package com.example.medtracker.data

data class TreatmentPost(
    val tName: String,
    val start_Time: String,
    val end_Time: String,
    val note: String,
    val doctorID: Int
)

