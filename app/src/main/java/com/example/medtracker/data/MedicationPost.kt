package com.example.medtracker.data

data class MedicationPost(
    val idTreatment: Int,
    val pName: String,
    val mDescription: String,
    val start_Time: String,
    val end_Time: String,
    val timeUse: String,
    val quantity: Int
)


