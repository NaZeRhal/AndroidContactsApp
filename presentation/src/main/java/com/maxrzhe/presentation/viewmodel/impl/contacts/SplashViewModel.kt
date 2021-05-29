package com.maxrzhe.presentation.viewmodel.impl.contacts

import com.maxrzhe.presentation.navigation.RouteDestination
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

class SplashViewModel : BaseViewModel() {

    fun navigateToHomeFragment() {
        navigateTo(RouteDestination.Contacts.HomeViewPager, null, false)
    }

    override fun onBackPressed() { }
}