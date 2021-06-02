package com.maxrzhe.presentation.viewmodel.impl.settings

import androidx.lifecycle.MutableLiveData
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.model.SettingsItemViewModel
import com.maxrzhe.presentation.navigation.RouteFragmentDestination
import com.maxrzhe.presentation.navigation.RouteSection
import com.maxrzhe.presentation.navigation.Router
import com.maxrzhe.presentation.util.AppResources
import com.maxrzhe.presentation.viewmodel.base.ViewModelWithRouter

class SettingsListViewModel(private val appResources: AppResources, router: Router) :
    ViewModelWithRouter(router) {

    val settingItems: MutableLiveData<List<SettingsItemViewModel>> = MutableLiveData()

    init {
        settingItems.value = getItems()
    }

    private fun getItems(): List<SettingsItemViewModel> = listOf(
        SettingsItemViewModel(
            iconId = R.drawable.ic_baseline_person_24,
            title = appResources.getString(R.string.settings_profile),
            clickListener = { navigateToProfile() }
        ),
        SettingsItemViewModel(
            iconId = R.drawable.ic_baseline_guide_24,
            title = appResources.getString(R.string.settings_guide),
            clickListener = { navigateToGuide() }
        ),
        SettingsItemViewModel(
            iconId = R.drawable.ic_baseline_feedback_24,
            title = appResources.getString(R.string.settings_feedback),
            clickListener = { navigateToFeedback() }
        ),
    )

    fun openContactsSection() {
        router.navigateTo(RouteSection.Contacts)
    }

    private fun navigateToProfile() {
        router.navigateTo(RouteFragmentDestination.Settings.ToProfile, false)
    }

    private fun navigateToGuide() {
        router.navigateTo(RouteFragmentDestination.Settings.ToGuide, false)
    }

    private fun navigateToFeedback() {
        router.navigateTo(RouteFragmentDestination.Settings.ToFeedback, false)
    }
}