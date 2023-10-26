package com.example.medtracker

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections

public class LogInFragmentDirections private constructor() {
  public companion object {
    public fun actionLogInFragmentToSignUpFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_logInFragment_to_signUpFragment)

    public fun actionLogInFragmentToMainFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_logInFragment_to_mainFragment)
  }
}
