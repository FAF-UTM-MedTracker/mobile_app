package com.example.medtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation

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
                // TODO: Add Update API call
                navController.navigate(R.id.action_treatmentFragment_to_mainFragment)
            }
        }else{
            addBtn.setOnClickListener {
                // TODO: Add Create API call
                navController.navigate(R.id.action_treatmentFragment_to_medicineFragment)
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