package com.example.medtracker

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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.medtracker.api.ApiManager
import com.example.medtracker.api.YourResponseModel
import com.example.medtracker.data.TreatmentListResponse
import com.example.medtracker.data.TreatmentPost
import com.example.medtracker.data.TreatmentUpdate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

/**
 * A simple [Fragment] subclass.
 * Use the [TreatmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TreatmentFragment : Fragment() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val call = ApiManager.apiService.getTreatments()

        val backBtn = getView()?.findViewById<View>(R.id.backBtn) as ImageButton
        backBtn.setOnClickListener {
            navController.navigate(R.id.action_treatmentFragment_to_mainFragment)
        }

        val addBtn = getView()?.findViewById<View>(R.id.saveTreatmentBtn) as Button
        if(arguments != null){
            val treatmentTitle = getView()?.findViewById(R.id.treatmentTitle) as TextView
            val treatmentName = getView()?.findViewById(R.id.treatmentName) as EditText
            val doctorName = getView()?.findViewById(R.id.doctorName) as EditText
            val treatmentDesc = getView()?.findViewById(R.id.description) as EditText

            // Change UI to inform user of creating changes
            treatmentTitle.text = "Edit Treatment"
            treatmentName.setText(arguments?.getString("tName"))
            doctorName.setText(arguments?.getString("doctorID").toString())
            treatmentDesc.setText(arguments?.getString("notePatient"))

            // Updating the Treatment and sending User back to main menu
            addBtn.setOnClickListener {
                val treatmentName = getView()?.findViewById(R.id.treatmentName) as EditText
                val doctorName = getView()?.findViewById(R.id.doctorName) as EditText
                val treatmentDesc = getView()?.findViewById(R.id.description) as EditText

                val treatment = TreatmentUpdate(
                    requireArguments().getInt("idTreatment"),
                    treatmentName.text.toString(),
                    requireArguments().getString("startTime", LocalDateTime.now().toString()),
                    requireArguments().getString("endTime", LocalDateTime.now().toString()),
                    treatmentDesc.text.toString(),
                    requireArguments().getInt("doctorID")
                )
                Log.v("Updating Treatment...", treatment.toString())

                ApiManager.apiService.updateTreatment(treatment).enqueue(object : Callback<YourResponseModel> {
                    override fun onResponse(call: Call<YourResponseModel>, response: Response<YourResponseModel>) {
                        Log.v("API Response", response.toString())
                        if(response.code() in 200..299){
                            // No response from API...
                            //navController.navigate(R.id.action_treatmentFragment_to_mainFragment)
                        }else{
                            // Show the error message somewhere...
                        }
                    }

                    override fun onFailure(call: Call<YourResponseModel>, t: Throwable) {
                        // Handle network failure
                        //TODO: Discuss about API response, remove this patchwork solution
                        Log.v("API Response", t.toString())
                        navController.navigate(R.id.action_treatmentFragment_to_mainFragment)
                    }
                })
            }
        }else{
            addBtn.setOnClickListener {
                //TODO: Clarify how to get DoctorID, replace hardcoded value 7
                val treatmentName = getView()?.findViewById(R.id.treatmentName) as EditText
                val doctorName = getView()?.findViewById(R.id.doctorName) as EditText
                val treatmentDesc = getView()?.findViewById(R.id.description) as EditText

                val treatment = TreatmentPost(
                    treatmentName.text.toString(),
                    LocalDateTime.now().toString(),
                    LocalDateTime.now().toString(),
                    treatmentDesc.text.toString(),
                    7
                )

                ApiManager.apiService.addTreatment(treatment).enqueue(object : Callback<YourResponseModel> {
                    override fun onResponse(call: Call<YourResponseModel>, response: Response<YourResponseModel>) {
                        if(response.code() in 200..299){
                            /*navController.navigate(R.id.action_treatmentFragment_to_addMedicineFragment,
                                bundleOf(
                                    "idTreatment" to idTreatment, <- Getting this is problematic...
                                    "tName" to treatmentName.text.toString(),
                                )
                            )*/
                            navController.navigate(R.id.action_treatmentFragment_to_mainFragment)
                        }else{
                            // Show the error message somewhere...
                        }
                    }

                    override fun onFailure(call: Call<YourResponseModel>, t: Throwable) {
                        // Handle network failure
                    }
                })
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_treatment, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment TreatmentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TreatmentFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}