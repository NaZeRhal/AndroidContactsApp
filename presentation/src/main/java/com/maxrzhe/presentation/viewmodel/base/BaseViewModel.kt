package com.maxrzhe.presentation.viewmodel.base

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.maxrzhe.common.util.SingleEvent
import com.maxrzhe.presentation.navigation.RouteDestination
import com.maxrzhe.presentation.navigation.RouteSection
import com.maxrzhe.presentation.navigation.defaultNavOptions

abstract class BaseViewModel : ViewModel() {

    val navigationEvent: MutableLiveData<SingleEvent<NavController.() -> Any>> = MutableLiveData()

    fun navigateTo(route: RouteSection, args: Bundle? = null) {
        withNavController { navigate(route.graph, args, defaultNavOptions) }
    }

    fun navigateTo(route: RouteDestination, args: Bundle? = null, clearStack: Boolean = false) {
        when {
            route is RouteDestination.Back -> withNavController { popBackStack() }
            clearStack -> withNavController { popBackStack(route.destination, false) }
            else -> withNavController { navigate(route.destination, args, defaultNavOptions) }
        }
    }

    protected fun withNavController(block: NavController.() -> Any) {
        navigationEvent.postValue(SingleEvent(block))
    }

    abstract fun onBackPressed()
}