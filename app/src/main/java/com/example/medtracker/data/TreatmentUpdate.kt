package com.example.medtracker.data

data class TreatmentUpdate(
    val treatmentId: Int,
    val name: String,
    val startTime: String,
    val endTime: String,
    val note: String,
    val doctorID: Int
)