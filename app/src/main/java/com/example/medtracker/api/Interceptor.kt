package com.example.medtracker.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val bearerToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $bearerToken")
            .build()

        return chain.proceed(modifiedRequest)
    }
}
