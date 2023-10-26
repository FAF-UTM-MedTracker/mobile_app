package com.example.medtracker

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections

public class SignUpFragmentDirections private constructor() {
  public companion object {
    public fun actionSignUpFragmentToLogInFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_signUpFragment_to_logInFragment)

    public fun actionSignUpFragmentToMainFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_signUpFragment_to_mainFragment)
  }
}
