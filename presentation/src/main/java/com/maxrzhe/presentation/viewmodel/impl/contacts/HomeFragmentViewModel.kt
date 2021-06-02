package com.maxrzhe.presentation.viewmodel.impl.contacts

import com.maxrzhe.presentation.navigation.RouteFragmentDestination
import com.maxrzhe.presentation.navigation.RouteSection
import com.maxrzhe.presentation.navigation.Router
import com.maxrzhe.presentation.viewmodel.base.ViewModelWithRouter

class HomeFragmentViewModel(router: Router) : ViewModelWithRouter(router) {

    fun addContactClick() {
        router.navigateTo(RouteFragmentDestination.Contacts.ToDetail(), false)
    }

    fun onExitApp() {
        router.navigateTo(RouteSection.ExitApp)
    }
}