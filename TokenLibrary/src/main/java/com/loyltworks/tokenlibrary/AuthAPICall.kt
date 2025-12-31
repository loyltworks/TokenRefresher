package com.loyltworks.tokenlibrary
import TokenResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException


class AuthAPICall {

    //OK HTTP CLIENT FOR PRINTING REQUEST AND RESPONSE WE ARE ADDING OKHTTP LOGIN

    private val client: OkHttpClient by lazy {
        val interCepter = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(interCepter)
            .build()
    }

   suspend fun run(url: String, requestBody: String): TokenResponse? {
       var refToken: TokenResponse? =null
        val mediaType = "application/x-www-from-urlencoded".toMediaTypeOrNull()
        val body = requestBody.toRequestBody(mediaType)

        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("content-type", "application/x-www-form-urlencoded")
            .addHeader("cache-control", "no-cache")
            .build()



        client.newCall(request).enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                    if(response.isSuccessful){
                        refToken = Moshi.Builder()
                            .add(KotlinJsonAdapterFactory())
                            .build()
                            .adapter(TokenResponse::class.java)
                            .fromJson(response.body?.source()?.buffer)!!
                    }
            }

        })

       return if(refToken!=null) refToken else null
}

}