package com.maxrzhe.presentation.viewmodel.impl

import com.maxrzhe.presentation.navigation.RouteSection
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

class HomeFragmentViewModel : BaseViewModel() {

    fun openSettings() {
        navigateTo(RouteSection.Settings, null)
    }
}