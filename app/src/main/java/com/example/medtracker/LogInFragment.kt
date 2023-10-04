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
import com.example.medtracker.data.LoginData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment() {
    private lateinit var navController: NavController
    val apiService = ApiManager.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val signUpText = getView()?.findViewById<View>(R.id.textView4) as TextView
        signUpText.setOnClickListener {
            navController.navigate(R.id.action_logInFragment_to_signUpFragment)
        }

        val loginBtn = getView()?.findViewById<View>(R.id.button) as Button
        loginBtn.setOnClickListener {

            if (checkInput(view)) {
                val mail = (getView()?.findViewById<View>(R.id.emailInput) as EditText).text.toString()
                val pass = (getView()?.findViewById<View>(R.id.passInput) as EditText).text.toString()
                val loginData = LoginData(mail, pass)

                var retVal = true
                val mailErr = getView()?.findViewById<View>(R.id.emailErr) as TextView
                val passErr = getView()?.findViewById<View>(R.id.passLogErr) as TextView

                apiService.login(loginData).enqueue(object : Callback<YourResponseModel> {
                    override fun onResponse(
                        call: Call<YourResponseModel>,
                        response: Response<YourResponseModel>
                    ) {
                        val responseBody = response.body()
                        val message = responseBody?.message
                        Log.d("API Response", "Message: ${message}")
                        if (responseBody != null && responseBody.message == "Login successful") {
                            navController.navigate(R.id.action_logInFragment_to_mainFragment)
                        } else {
                            passErr.text = "Incorrect data"
                            retVal = false
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
        val mail = (getView()?.findViewById<View>(R.id.emailInput) as EditText).text.toString()
        val pass = (getView()?.findViewById<View>(R.id.passInput) as EditText).text.toString()
        val mailErr = getView()?.findViewById<View>(R.id.emailErr) as TextView
        val passErr = getView()?.findViewById<View>(R.id.passLogErr) as TextView

        if (mail == "") {
            mailErr.text = "Missing email address"
            retVal = false
        } else if (!mail.matches(Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"))) {
            mailErr.text = "Invalid email address"
            retVal = false
        } else {
            mailErr.text = ""
        }

        if (pass == "") {
            passErr.text = "Missing password"
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
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment LogInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            LogInFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}