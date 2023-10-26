package com.example.medtracker.data

data class DoctorResponse (
    val doctorId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String
)