package com.maxrzhe.presentation.viewmodel.impl.contacts

import com.maxrzhe.presentation.navigation.RouteDestination
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

class HomeFragmentViewModel : BaseViewModel() {

    fun addContactClick() {
        navigateTo(RouteDestination.Contacts.Detail, null, false)
    }

    override fun onBackPressed() {
        onAppExit()
    }

    private fun onAppExit() {

    }
}