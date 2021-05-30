package com.maxrzhe.presentation.viewmodel.impl.contacts

import com.maxrzhe.presentation.navigation.RouteFragmentDestination
import com.maxrzhe.presentation.navigation.RouteSection
import com.maxrzhe.presentation.navigation.Router
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

class HomeFragmentViewModel(router: Router) : BaseViewModel(router) {

    fun addContactClick() {
        router.navigateTo(RouteFragmentDestination.Contacts.Detail, null, false)
    }

    fun onExitApp() {
        router.navigateTo(RouteSection.ExitApp)
    }
}