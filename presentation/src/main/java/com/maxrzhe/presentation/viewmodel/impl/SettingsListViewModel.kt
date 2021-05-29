package com.maxrzhe.presentation.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.model.SettingsItemViewModel
import com.maxrzhe.presentation.navigation.RouteDestination
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

class SettingsListViewModel : BaseViewModel() {

    val settingItems: MutableLiveData<List<SettingsItemViewModel>> = MutableLiveData()

    init {
        settingItems.value = getItems()
    }

    private fun getItems(): List<SettingsItemViewModel> = listOf(
        SettingsItemViewModel(
            iconId = R.drawable.ic_baseline_person_24,
            title = "Profile",
            clickListener = { navigateToProfile() }
        ),
        SettingsItemViewModel(
            iconId = R.drawable.ic_baseline_guide_24,
            title = "Guide",
            clickListener = { navigateToGuide() }
        ),
        SettingsItemViewModel(
            iconId = R.drawable.ic_baseline_feedback_24,
            title = "Feedback",
            clickListener = { navigateToFeedback() }
        ),
    )

    private fun navigateToProfile() {
        navigateTo(RouteDestination.Settings.Profile, null, false)
    }

    private fun navigateToGuide() {
        navigateTo(RouteDestination.Settings.Guide, null, false)
    }

    private fun navigateToFeedback() {
        navigateTo(RouteDestination.Settings.Feedback, null, false)
    }
}