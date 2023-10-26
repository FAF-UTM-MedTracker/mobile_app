package com.example.medtracker

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections

public class AddMedicineFragmentDirections private constructor() {
  public companion object {
    public fun actionAddMedicineFragmentToMedicineFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_addMedicineFragment_to_medicineFragment)
  }
}
