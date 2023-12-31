package com.example.medtracker

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.medtracker.api.ApiManager
import com.example.medtracker.api.YourResponseModel
import com.example.medtracker.data.MedicationPost
import com.example.medtracker.data.MedicationUpdate
import com.example.medtracker.data.TreatmentListResponse
import com.example.medtracker.data.TreatmentUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddMedicineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddMedicineFragment : Fragment() {
    private lateinit var navController: NavController
    private val apiService = ApiManager.apiService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val backBtn = getView()?.findViewById<View>(R.id.backBtn) as ImageButton
        backBtn.setOnClickListener {
            navController.navigate(R.id.action_addMedicineFragment_to_medicineFragment,
                bundleOf(
                    "idTreatment" to requireArguments().getInt("idTreatment"),
                    "tName" to requireArguments().getString("tName"))
            )
        }

        if (requireArguments().getString("goal") == "update"){
            val medicineName = getView()?.findViewById<View>(R.id.medicationName) as EditText
            val startDate = getView()?.findViewById<View>(R.id.startDate) as EditText
            val endDate = getView()?.findViewById<View>(R.id.endDate) as EditText
            val hour = getView()?.findViewById<View>(R.id.timeInput) as EditText
            val quantity = getView()?.findViewById<View>(R.id.qty) as EditText
            val description = getView()?.findViewById<View>(R.id.description) as EditText

            medicineName.setText(requireArguments().getString("name"))
            startDate.setText(requireArguments().getString("startTime"))
            endDate.setText(requireArguments().getString("endTime"))
            hour.setText(requireArguments().getString("timeUse"))
            quantity.setText(requireArguments().getInt("quantity").toString())
            description.setText(requireArguments().getString("description"))
        }

        val saveButton = getView()?.findViewById<View>(R.id.saveMedBtn) as Button
        saveButton.setOnClickListener {
            if (checkInput(view)){
                val medicineName = (getView()?.findViewById<View>(R.id.medicationName) as EditText).text.toString()
                val startDate = (getView()?.findViewById<View>(R.id.startDate) as EditText).text.toString()
                val endDate = (getView()?.findViewById<View>(R.id.endDate) as EditText).text.toString()
                val hour = startDate + "T" + (getView()?.findViewById<View>(R.id.timeInput) as EditText).text.toString()
                val quantity = (getView()?.findViewById<View>(R.id.qty) as EditText).text.toString().toInt()
                val description = (getView()?.findViewById<View>(R.id.description) as EditText).text.toString()
                val idTreatment = requireArguments().getInt("idTreatment")

                if (requireArguments().getString("goal") == "update") {
                    val requestBody = MedicationUpdate(requireArguments().getInt("idMedication"), medicineName, description, startDate, endDate, hour, quantity)
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = apiService.updateMedication(requestBody).execute()
                            val statusCode = response.code()
                            if (statusCode in 200..299) {
                                withContext(Dispatchers.Main) {
                                    navController.navigate(R.id.action_addMedicineFragment_to_medicineFragment,
                                        bundleOf(
                                            "idTreatment" to requireArguments().getInt("idTreatment"),
                                            "tName" to requireArguments().getString("tName")
                                        )
                                    )
                                }
                            }
                        } catch (e: IOException) {
                            // Handle the exception
                        }
                    }
                } else {
                    val requestBody = MedicationPost(idTreatment, medicineName, description, startDate, endDate, hour, quantity)
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = apiService.addMedication(requestBody).execute()
                            val statusCode = response.code()
                            if (statusCode in 200..299) {
                                withContext(Dispatchers.Main) {
                                    navController.navigate(R.id.action_addMedicineFragment_to_medicineFragment,
                                        bundleOf(
                                            "idTreatment" to requireArguments().getInt("idTreatment"),
                                            "tName" to requireArguments().getString("tName")
                                        )
                                    )
                                }
                            }
                        } catch (e: IOException) {
                            // Handle the exception
                        }
                    }
                }

                //Update the treatment's Start and End times too, if needed
                ApiManager.apiService.getTreatments().enqueue(object : Callback<List<TreatmentListResponse>> {
                    override fun onResponse(call: Call<List<TreatmentListResponse>>,
                                            response: Response<List<TreatmentListResponse>>
                    ) {
                        if (response.isSuccessful) {
                            val treatmentList = response.body()!!

                            for (treatment in treatmentList) {
                                if(treatment.idTreatment == idTreatment){
                                    updateTreatment(treatment)
                                    break
                                }
                            }
                        } else {
                            // Handle the error response
                        }
                    }

                    override fun onFailure(call: Call<List<TreatmentListResponse>>, t: Throwable) {}
                })
            }
        }
    }

    private fun updateTreatment(treatment: TreatmentListResponse){
        //updateTreatment(treatment, startDate, endDate)
        var minTime = LocalDate.parse("9999-12-30")
        var maxTime = LocalDate.parse("1000-01-01")
        for (medicine in treatment.medications!!){
            var targetDate = LocalDate.parse(medicine.start_Time.substring(0, 10))
            var delta = minTime.until(targetDate, ChronoUnit.DAYS)
            if(delta < 0){
                minTime = targetDate
            }

            targetDate = LocalDate.parse(medicine.end_Time.substring(0, 10))
            delta = maxTime.until(targetDate, ChronoUnit.DAYS)
            if(delta > 0){
                maxTime = targetDate
            }
        }

        var tUpdate = TreatmentUpdate(
            treatment.idTreatment,
            treatment.tName,
            minTime.toString()+"T00:00:00",
            maxTime.toString()+"T00:00:00",
            treatment.notePatient,
            treatment.doctorID
        )

        ApiManager.apiService.updateTreatment(tUpdate).enqueue(object : Callback<YourResponseModel> {
            override fun onResponse(call: Call<YourResponseModel>, response: Response<YourResponseModel>) {
                if(response.code() in 200..299){
                    // Success!
                }else{
                    // Show the error message somewhere...
                }
            }

            override fun onFailure(call: Call<YourResponseModel>, t: Throwable) {}
        })
    }

    private fun checkInput(view: View): Boolean {
        var retVal = true
        val medName = (getView()?.findViewById<View>(R.id.medicationName) as EditText).text.toString()
        val startDate = (getView()?.findViewById<View>(R.id.startDate) as EditText).text.toString()
        val endDate = (getView()?.findViewById<View>(R.id.endDate) as EditText).text.toString()
        val hour = (getView()?.findViewById<View>(R.id.timeInput) as EditText).text.toString()
        val quantity = (getView()?.findViewById<View>(R.id.qty) as EditText).text.toString()

        val nameErr = getView()?.findViewById<View>(R.id.medNameErr) as TextView
        val startDateErr = getView()?.findViewById<View>(R.id.startDateErr) as TextView
        val endDateErr = getView()?.findViewById<View>(R.id.endDateErr) as TextView
        val hourErr = getView()?.findViewById<View>(R.id.hourErr) as TextView
        val quantityErr = getView()?.findViewById<View>(R.id.qtyErr) as TextView

        if (medName == "") {
            nameErr.text = "   Missing name"
            retVal = false
        } else {
            nameErr.text = ""
        }

        var startDateValid = false
        if (startDate == "") {
            startDateErr.text = "   Missing start date"
            retVal = false
        } else if (!startDate.matches(Regex("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\$"))) {
            startDateErr.text = "   Invalid date"
            retVal = false
        } else {
            startDateErr.text = ""
            startDateValid = true
        }

        if (endDate == "") {
            endDateErr.text = "   Missing end date"
            retVal = false
        } else if (!endDate.matches(Regex("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\$"))) {
            endDateErr.text = "   Invalid date"
            retVal = false
        } else if (startDateValid) {
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date1 = LocalDate.parse(startDate, dateFormatter)
            val date2 = LocalDate.parse(endDate, dateFormatter)
            val daysDifference = ChronoUnit.DAYS.between(date1, date2)
            if (daysDifference < 0) {
                endDateErr.text = "   End date before start date"
                retVal = false
            } else {
                endDateErr.text = ""
            }
        } else {
            endDateErr.text = ""
        }

        if (hour == "") {
            hourErr.text = "   Missing time"
            retVal = false
        } else if (!hour.matches(Regex("^([01]\\d|2[0-3]):([0-5]\\d)$"))) {
            hourErr.text = "   Invalid time"
            retVal = false
        } else {
            hourErr.text = ""
        }

        if (quantity == "") {
            quantityErr.text = "   Missing quantity"
            retVal = false
        } else {
            quantityErr.text = ""
        }

        return retVal
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_medicine, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddMedicineFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddMedicineFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}