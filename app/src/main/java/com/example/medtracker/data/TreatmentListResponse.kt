package com.example.medtracker.data

data class TreatmentListResponse(
    val idTreatment: Int,
    val tName: String,
    val statusTreatment: String,
    val start_Time: String,
    val end_Time: String,
    val notePatient: String,
    val noteDoctor: String?,
    val doctorID: Int,
    val medications: List<Medication>?
)

data class Medication(
    val idTreatment: Int,
    val idMedication: Int,
    val pName: String,
    val mDescription: String,
    val start_Time: String,
    val end_Time: String,
    val timeUse: String,
    val quantity: Int
)
