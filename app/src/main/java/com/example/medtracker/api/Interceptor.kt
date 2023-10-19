package com.example.medtracker.api

import android.content.Context
import android.util.Log
import com.example.medtracker.YourApplicationClass
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val bearerToken = getBearerToken()

        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $bearerToken")
            .build()

        return chain.proceed(modifiedRequest)
    }

    fun getBearerToken(): String {
        val sharedPreferences = YourApplicationClass.getContext().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        Log.d("APIIIIII Response", "Message: ${sharedPreferences.getString("bearerToken", "") ?: ""}")
        return sharedPreferences.getString("bearerToken", "") ?: ""
    }
}
