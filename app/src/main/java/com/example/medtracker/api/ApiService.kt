package com.example.medtracker.api

import com.example.medtracker.data.LoginData
import com.example.medtracker.data.MedicationPost
import com.example.medtracker.data.TreatmentListResponse
import com.example.medtracker.data.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/register/patient")
    fun createUser(@Body userData: UserData): Call<YourResponseModel>

    @POST("auth/login")
    fun login(@Body loginData: LoginData): Call<YourResponseModel>

    @POST("/Patient/AddMedication")
    fun addMedication(@Body medication: MedicationPost): Call<YourResponseModel>

    @GET("/Patient/GetTreatments")
    fun getTreatments(): Call<List<TreatmentListResponse>>
}
