package com.example.medtracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.medtracker.api.ApiManager
import com.example.medtracker.api.YourResponseModel
import com.example.medtracker.data.TreatmentListResponse
import com.example.medtracker.data.TreatmentRemove
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    private lateinit var navController: NavController
    private val apiService = ApiManager.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val addBtn = getView()?.findViewById<View>(R.id.addTreatment) as ImageButton
        addBtn.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_treatmentFragment)
        }

        getTreatments()
    }

    // Function to get the list of medications
    private fun getTreatments() {
        val call = ApiManager.apiService.getTreatments()

        call.enqueue(object : Callback<List<TreatmentListResponse>> {
            override fun onResponse(call: Call<List<TreatmentListResponse>>,
                                    response: Response<List<TreatmentListResponse>>
            ) {
                if (response.isSuccessful) {
                    val treatmentList = response.body()!!
                    val menuLayout = getView()?.findViewById(R.id.menu_layout) as LinearLayout

                    for (treatment in treatmentList) {
                        drawTreatment(treatment, menuLayout)
                    }
                    Log.v("API Data", "Data: $treatmentList")
                } else {
                    // Handle the error response
                }
            }

            override fun onFailure(call: Call<List<TreatmentListResponse>>, t: Throwable) {
                Log.e("Treatment Get", "Request failed with error: ${t.message}")
            }
        })
    }

    private fun drawTreatment(treatment: TreatmentListResponse, menuLayout: LinearLayout){
        val item = LayoutInflater.from(context).inflate(R.layout.treatment_template,null)

        val itemName = item.findViewById(R.id.treatmentName1) as TextView
        val nextName = item.findViewById(R.id.nextMed1) as TextView
        val itemDates = item.findViewById(R.id.dates1) as TextView
        val itemHour = item.findViewById(R.id.hour1) as TextView
        val editBtn = item.findViewById(R.id.editBtn1) as ImageButton
        val delBtn = item.findViewById(R.id.delBtn1) as ImageButton

        itemName.text = treatment.tName
        itemDates.text = "Duration: " + treatment.start_Time.substring(0, 10) + " - " + treatment.end_Time.substring(0, 10)

        var minHour = 9999999999
        var targetHour = "00:00"
        var targetMed = "None"
        var targetQty = 0

        for (medicine in treatment.medications!!){
            var delta = LocalDateTime.now().until(LocalDateTime.parse(medicine.timeUse), ChronoUnit.HOURS)
            if(delta < 0){
                // WARNING: Remember to cover for cases where delta can be negative
                // Delta is negative if a medicine is overdue (its timeUse is in the past)
                // Either update data in the API or increment date by 1 day/week
            }
            if(delta < minHour){
                minHour = delta
                targetHour = medicine.timeUse.substring(11, 16)
                targetMed = medicine.pName
                targetQty = medicine.quantity
            }
        }

        itemHour.text = targetHour
        nextName.text = targetMed + " - " + targetQty

        editBtn.setOnClickListener{
            navController.navigate(R.id.action_mainFragment_to_treatmentFragment,
                bundleOf(
                    "idTreatment" to treatment.idTreatment,
                    "tName" to treatment.tName,
                    "doctorID" to treatment.doctorID,
                    "startTime" to treatment.start_Time,
                    "endTime" to treatment.end_Time,
                    "notePatient" to treatment.notePatient)
            )
        }

        delBtn.setOnClickListener {
            //Send req to API to remove this treatment
            val removeID = TreatmentRemove(treatment.idTreatment)
            apiService.removeTreatment(removeID).enqueue(object : Callback<YourResponseModel> {
                override fun onResponse(call: Call<YourResponseModel>, response: Response<YourResponseModel>) {
                    if(response.isSuccessful){
                        // TODO: Update the UI properly as treatment gets removed
                        (item.parent as ViewGroup).removeView(item)
                    }else{
                        // Show the error message somewhere...
                    }
                }

                override fun onFailure(call: Call<YourResponseModel>, t: Throwable) {
                    // Handle network failure
                }
            })
        }

        item.setOnClickListener{
            navController.navigate(R.id.action_mainFragment_to_medicineFragment,
                bundleOf("idTreatment" to treatment.idTreatment, "tName" to treatment.tName)
            )
        }

        menuLayout.addView(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            MainFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}