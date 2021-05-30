package com.maxrzhe.presentation.viewmodel.impl.contacts

import com.maxrzhe.presentation.navigation.RouteSection
import com.maxrzhe.presentation.navigation.Router
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

class ContactsActivityViewModel(router: Router) : BaseViewModel(router) {

    fun openSettingsSection() {
        router.navigateTo(RouteSection.Settings)
    }

    fun openVolumeSettings() {
        router.navigateTo(RouteSection.VolumeSetting)
    }
}