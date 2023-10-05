package com.example.medtracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.medtracker.api.ApiManager
import com.example.medtracker.api.YourResponseModel
import com.example.medtracker.data.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {
    private lateinit var navController: NavController
    val apiService = ApiManager.apiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val loginText = getView()?.findViewById<View>(R.id.textView4) as TextView
        loginText.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_logInFragment)
        }

        val signUpBtn = getView()?.findViewById<View>(R.id.button) as Button
        signUpBtn.setOnClickListener {
            if (checkInput(view)) {
                val name = (getView()?.findViewById<View>(R.id.nameInput) as EditText).text.toString()
                val surname = (getView()?.findViewById<View>(R.id.surnameInput) as EditText).text.toString()
                val mail = (getView()?.findViewById<View>(R.id.mailInput) as EditText).text.toString()
                val phone = (getView()?.findViewById<View>(R.id.phoneInput) as EditText).text.toString()
                val date = (getView()?.findViewById<View>(R.id.birthdayInput) as EditText).text.toString()
                val pass = (getView()?.findViewById<View>(R.id.passInput) as EditText).text.toString()

                val userData = UserData(mail, pass, name, surname, phone, date)

                val mailErr = getView()?.findViewById<View>(R.id.mailErr) as TextView
                var retVal = true

                apiService.createUser(userData).enqueue(object : Callback<YourResponseModel> {
                    override fun onResponse(
                        call: Call<YourResponseModel>,
                        response: Response<YourResponseModel>
                    ) {
                        if (response.isSuccessful){
                            val responseBody = response.body()
                            if (responseBody != null && responseBody.message == "Registration successful") {
                                navController.navigate(R.id.action_signUpFragment_to_mainFragment)
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            if (errorBody != null && errorBody.contains("Email address is already in use.")) {
                                mailErr.text = "Email already in use"
                                retVal = false
                            }
                        }
                    }
                    override fun onFailure(call: Call<YourResponseModel>, t: Throwable) {
                        // Handle network failure
                        // You can show a network error message to the user
                    }
                })
            }
        }
    }

    private fun checkInput(view: View): Boolean {
        var retVal = true
        val name = (getView()?.findViewById<View>(R.id.nameInput) as EditText).text.toString()
        val surname = (getView()?.findViewById<View>(R.id.surnameInput) as EditText).text.toString()
        val mail = (getView()?.findViewById<View>(R.id.mailInput) as EditText).text.toString()
        val phone = (getView()?.findViewById<View>(R.id.phoneInput) as EditText).text.toString()
        val date = (getView()?.findViewById<View>(R.id.birthdayInput) as EditText).text.toString()
        val pass = (getView()?.findViewById<View>(R.id.passInput) as EditText).text.toString()

        val nameErr = getView()?.findViewById<View>(R.id.nameErr) as TextView
        val mailErr = getView()?.findViewById<View>(R.id.mailErr) as TextView
        val phoneErr = getView()?.findViewById<View>(R.id.phoneErr) as TextView
        val dateErr = getView()?.findViewById<View>(R.id.dateErr) as TextView
        val passErr = getView()?.findViewById<View>(R.id.passErr) as TextView

        if (name == "" || surname == "") {
            nameErr.text = "Missing a name"
            retVal = false
        } else if (!name.matches(Regex("^[a-zA-Z'-]+\$")) ||
            !surname.matches(Regex("^[a-zA-Z'-]+\$"))
        ) {
            nameErr.text = "Use alphabetical characters only"
            retVal = false
        } else {
            nameErr.text = ""
        }

        if (mail == "") {
            mailErr.text = "Missing email address"
            retVal = false
        } else if (!mail.matches(Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"))) {
            mailErr.text = "Invalid email address"
            retVal = false
        } else {
            mailErr.text = ""
        }

        if (phone == "") {
            phoneErr.text = "Missing phone number"
            retVal = false
        } else if (!phone.matches(Regex("^((\\+\\d{1,3})|0)\\d{8}\$"))) {
            phoneErr.text = "Invalid phone number"
            retVal = false
        } else {
            phoneErr.text = ""
        }

        if (date == "") {
            dateErr.text = "Missing birthday"
            retVal = false
        } else if (!date.matches(Regex("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\$"))) {
            dateErr.text = "Invalid date"
            retVal = false
        } else {
            dateErr.text = ""
        }

        if (pass == "") {
            passErr.text = "Missing password"
            retVal = false
        } else if (pass.length < 8) {
            passErr.text = "Password too short, 8 characters minimum"
            retVal = false
        } else {
            passErr.text = ""
        }

        return retVal
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SignUpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}