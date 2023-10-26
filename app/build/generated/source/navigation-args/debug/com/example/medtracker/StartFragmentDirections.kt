package com.example.medtracker

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections

public class StartFragmentDirections private constructor() {
  public companion object {
    public fun actionStartFragmentToLogInFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_startFragment_to_logInFragment)

    public fun actionStartFragmentToSignUpFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_startFragment_to_signUpFragment)
  }
}
