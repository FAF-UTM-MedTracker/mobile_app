package com.example.medtracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.medtracker.api.ApiManager
import com.example.medtracker.data.TreatmentListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MedicineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MedicineFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private  lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val addBtn = getView()?.findViewById<View>(R.id.addTreatment) as ImageButton
        addBtn.setOnClickListener {
            navController.navigate(R.id.action_medicineFragment_to_addMedicineFragment)
        }

        val backBtn = getView()?.findViewById<View>(R.id.backBtn) as ImageButton
        backBtn.setOnClickListener {
            navController.navigate(R.id.action_medicineFragment_to_mainFragment)
        }

        val treatmentId = 1
        getMedications(treatmentId)
    }


    // Function to get the list of medications
    fun getMedications(treatmentId: Int) {
        val call = ApiManager.apiService.getTreatments()
        Log.d("Medications1", "Response is ${call}")

        call.enqueue(object : Callback<List<TreatmentListResponse>> {
            override fun onResponse(call: Call<List<TreatmentListResponse>>, response: Response<List<TreatmentListResponse>>) {
                if (response.isSuccessful) {
                    Log.d("Medications3", "Response is ${response.body()}")

                    val treatment = response.body()?.get(treatmentId)
                    if (treatment != null) {
                        for (medication in treatment.medications!!){
                            // Add the medicationRectangle to your UI
                            addMedicationToUI(
                                medication.pName,
                                medication.start_Time.substring(0, 10),
                                medication.end_Time.substring(0, 10),
                                medication.timeUse.substring(11, 16)
                            )
                        }
                    }
                } else {
                    // Handle the error response
                }
            }

            override fun onFailure(call: Call<List<TreatmentListResponse>>, t: Throwable) {
                // suck it
                Log.e("Medications3", "Request failed with error: ${t.message}")

            }
        })
    }

    private fun addMedicationToUI(name: String, start: String, end: String, time: String) {
        // Inflate the medication rectangle layout
        val medicationRectangle = layoutInflater.inflate(R.layout.medicine_item, null) as ConstraintLayout

        // Populate the medication rectangle with data
        val medName = medicationRectangle.findViewById<TextView>(R.id.medName)
        val dates = medicationRectangle.findViewById<TextView>(R.id.dates)
        val hour = medicationRectangle.findViewById<TextView>(R.id.hour)
        //val interval = medicationRectangle.findViewById<TextView>(R.id.interval)

        medName.text = name
        dates.text = "$start - $end"
        hour.text = time
        // Set the interval accordingly

        // Find your ScrollView (replace with the actual ID)
        val scrollView = view?.findViewById<ScrollView>(R.id.main_scroll_view)

        // Add the medication rectangle to the ScrollView
        val medicationContainer = view?.findViewById<LinearLayout>(R.id.medicationContainer)
        medicationContainer?.addView(medicationRectangle)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medicine, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MedicineFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MedicineFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}