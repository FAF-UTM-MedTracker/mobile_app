package com.example.medtracker

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections

public class MedicineFragmentDirections private constructor() {
  public companion object {
    public fun actionMedicineFragmentToAddMedicineFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_medicineFragment_to_addMedicineFragment)

    public fun actionMedicineFragmentToMainFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_medicineFragment_to_mainFragment)
  }
}
