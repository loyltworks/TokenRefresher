package com.loyltworks.tokenlibrary.localprefrencehelper

import TokenResponse
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

object TokenPrefrenceHelper {

    private fun getPrefrenceHelper(context: Context):SharedPreferences{
        val masterKey=MasterKey.Builder(context.applicationContext).setKeyScheme(
            MasterKey.KeyScheme.AES256_GCM
        ).build()

        return  EncryptedSharedPreferences.create(
            context,
            "secure_satyam_prefrence",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun setTokenDetails(context: Context, token: TokenResponse){
        val jsonAdapter: JsonAdapter<TokenResponse> = jsonAdapter()
        val json = jsonAdapter.toJson(token)
        val sharedPreferences = getPrefrenceHelper(context)
        sharedPreferences.edit().putString("token", json).apply()
    }

    fun getTokenDetails(context: Context) : TokenResponse? {
        val sharedPreferences =  getPrefrenceHelper(context)
        val stringValue = sharedPreferences.getString("token", "")

        val jsonAdapter: JsonAdapter<TokenResponse> = jsonAdapter()
        return if(!stringValue.isNullOrEmpty()) {
            jsonAdapter.fromJson(stringValue)
        }else{
            null
        }
    }

    private fun jsonAdapter(): JsonAdapter<TokenResponse> {
        val moshi = Moshi.Builder().build()
        return moshi.adapter(TokenResponse::class.java)
    }

}