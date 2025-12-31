package com.loyltworks.tokenlibrary.data

import TokenResponse
import android.content.Context
import com.loyltworks.tokenlibrary.localprefrencehelper.TokenPrefrenceHelper
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor

object AuthAPICall {

    //OK HTTP CLIENT FOR PRINTING REQUEST AND RESPONSE WE ARE ADDING OKHTTP LOGIN

    private val client: OkHttpClient by lazy {
        val interCepter = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(interCepter)
            .build()
    }

    suspend fun run(context: Context, url: String, requestBody: String): TokenResponse? {
        val mediaType = "application/x-www-from-urlencoded".toMediaTypeOrNull()
        val body = requestBody.toRequestBody(mediaType)

        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("content-type", "application/x-www-form-urlencoded")
            .addHeader("cache-control", "no-cache")
            .build()

        client.newCall(request).execute().use { response ->
            return if(response.isSuccessful) {
                val token_data = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                    .adapter(TokenResponse::class.java)
                    .fromJson(response.body?.source()?.buffer)

                TokenPrefrenceHelper.setTokenDetails(context, token_data!!)

                token_data
            }else {
                null
            }
        }


    }
}