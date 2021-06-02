package com.maxrzhe.presentation.viewmodel.impl.contacts

import com.maxrzhe.presentation.navigation.RouteSection
import com.maxrzhe.presentation.navigation.Router
import com.maxrzhe.presentation.viewmodel.base.ViewModelWithRouter

class ContactsActivityViewModel(router: Router) : ViewModelWithRouter(router) {

    fun openSettingsSection() {
        router.navigateTo(RouteSection.Settings)
    }

    fun openVolumeSettings() {
        router.navigateTo(RouteSection.VolumeSetting)
    }
}