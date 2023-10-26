package com.example.medtracker

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections

public class MainFragmentDirections private constructor() {
  public companion object {
    public fun actionMainFragmentToTreatmentFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_mainFragment_to_treatmentFragment)

    public fun actionMainFragmentToMedicineFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_mainFragment_to_medicineFragment)
  }
}
