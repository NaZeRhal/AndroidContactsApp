package com.maxrzhe.presentation.viewmodel.impl.contacts

import com.maxrzhe.presentation.navigation.RouteFragmentDestination
import com.maxrzhe.presentation.navigation.Router
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

class SplashViewModel(router: Router) : BaseViewModel(router) {

    fun navigateToHomeFragment() {
        router.navigateTo(RouteFragmentDestination.Contacts.HomeViewPager, null, false)
    }
}