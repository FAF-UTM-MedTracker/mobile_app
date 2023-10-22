package com.example.medtracker.data

data class MedicationUpdate(
    val idMedication: Int,
    val name: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val timeUse: String,
    val quantity: Int
)