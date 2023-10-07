package com.example.medtracker.api

import com.example.medtracker.data.UserData
import com.example.medtracker.data.LoginData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register/patient")
    fun createUser(@Body userData: UserData): Call<YourResponseModel>

    @POST("auth/login")
    fun login(@Body loginData: LoginData): Call<YourResponseModel>
}
