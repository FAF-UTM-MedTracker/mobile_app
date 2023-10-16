package com.example.medtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.medtracker.api.ApiManager
import com.example.medtracker.api.YourResponseModel
import com.example.medtracker.data.MedicationPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val backBtn = getView()?.findViewById<View>(R.id.backBtn) as ImageButton
        backBtn.setOnClickListener {
            navController.navigate(R.id.action_addMedicineFragment_to_medicineFragment)
        }
    }


    // Function to add a medication
    private fun addMedication(medication: MedicationPost) {
        val call = ApiManager.apiService.addMedication(medication)

        call.enqueue(object : Callback<YourResponseModel> {
            override fun onResponse(call: Call<YourResponseModel>, response: Response<YourResponseModel>) {
                if (response.isSuccessful) {
                    val message = response.body()?.message
                    // Handle the success response
                } else {
                    // Handle the error response
                }
            }

            override fun onFailure(call: Call<YourResponseModel>, t: Throwable) {
                // Handle the network failure
            }
        })
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