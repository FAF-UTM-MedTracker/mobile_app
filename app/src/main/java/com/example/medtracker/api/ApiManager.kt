package com.example.medtracker.api


import com.example.medtracker.LogInFragment
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiManager {
    private const val BASE_URL = "https://medtrackerapi.azurewebsites.net/"

    // Create an instance of OkHttpClient with the AuthInterceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(getBearerToken()))
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        //.client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getBearerToken(): String {
        return LogInFragment().bearerToken
    }

    val apiService: ApiService = retrofit.create(ApiService::class.java)

}
