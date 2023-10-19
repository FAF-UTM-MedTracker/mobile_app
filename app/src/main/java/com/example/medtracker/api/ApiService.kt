package com.example.medtracker.api

import com.example.medtracker.data.LoginData
import com.example.medtracker.data.MedicationPost
import com.example.medtracker.data.TreatmentListResponse
import com.example.medtracker.data.TreatmentPost
import com.example.medtracker.data.TreatmentRemove
import com.example.medtracker.data.TreatmentUpdate
import com.example.medtracker.data.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register/patient")
    fun createUser(@Body userData: UserData): Call<YourResponseModel>

    @POST("auth/login")
    fun login(@Body loginData: LoginData): Call<YourResponseModel>

    @POST("/Patient/AddMedication")
    fun addMedication(@Body medication: MedicationPost): Call<YourResponseModel>

    @POST("/Patient/AddTreatment")
    fun addTreatment(@Body treatment: TreatmentPost): Call<YourResponseModel>

    @POST("/Patient/RemoveTreatment")
    fun removeTreatment(@Body treatment: TreatmentRemove): Call<YourResponseModel>

    @POST("/Patient/UpdateTreatment")
    fun updateTreatment(@Body treatment: TreatmentUpdate): Call<YourResponseModel>

    @GET("/Patient/GetTreatments")
    fun getTreatments(): Call<List<TreatmentListResponse>>
}
