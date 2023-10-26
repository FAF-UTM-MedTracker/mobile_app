package com.example.medtracker

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections

public class TreatmentFragmentDirections private constructor() {
  public companion object {
    public fun actionTreatmentFragmentToMainFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_treatmentFragment_to_mainFragment)
  }
}
