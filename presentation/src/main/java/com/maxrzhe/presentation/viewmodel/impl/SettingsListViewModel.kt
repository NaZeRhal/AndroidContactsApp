package com.maxrzhe.presentation.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.model.SettingsItemViewModel
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

class SettingsListViewModel : BaseViewModel() {

    val settingItems: MutableLiveData<List<SettingsItemViewModel>> = MutableLiveData()

    init {
        settingItems.value = getItems()
    }

    private fun getItems(): List<SettingsItemViewModel> = listOf(
        SettingsItemViewModel(
            iconId = R.drawable.ic_baseline_person_24,
            pathId = R.id.action_settingsFragment_to_profileFragment,
            title = "Profile",
            Navigation.createNavigateOnClickListener(R.id.action_settingsFragment_to_profileFragment)
        ),
        SettingsItemViewModel(
            iconId = R.drawable.ic_baseline_guide_24,
            pathId = R.id.action_settingsFragment_to_guideFragment,
            title = "Guide",
            Navigation.createNavigateOnClickListener(R.id.action_settingsFragment_to_guideFragment)
        ),
        SettingsItemViewModel(
            iconId = R.drawable.ic_baseline_feedback_24,
            pathId = R.id.action_settingsFragment_to_feedbackFragment,
            title = "Feedback",
            Navigation.createNavigateOnClickListener(R.id.action_settingsFragment_to_feedbackFragment)
        ),
    )
}