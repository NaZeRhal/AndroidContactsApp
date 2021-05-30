package com.maxrzhe.presentation.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.maxrzhe.common.util.SingleEvent

class Router {

    val fragmentNavigationEvent: MutableLiveData<SingleEvent<NavController.() -> Any>> =
        MutableLiveData()
    val activityNavigationEvent: MutableLiveData<SingleEvent<Activity.() -> Any>> =
        MutableLiveData()

    fun navigateTo(
        route: RouteFragmentDestination,
        args: Bundle? = null,
        clearStack: Boolean = false
    ) {
        when {
            route is RouteFragmentDestination.Back -> withNavController { popBackStack() }
            clearStack -> withNavController { popBackStack(route.destination, false) }
            else -> withNavController { navigate(route.destination, args, defaultNavOptions) }
        }
    }

    fun navigateTo(
        route: RouteSection,
        intentSetter: ((Intent) -> Unit)? = null,
        performFinish: Boolean = false
    ) {
        withActivity {
            when {
                route is RouteSection.ExitApp -> this.finish()
                route.activityClass != null -> {
                    val intent = Intent(this, route.activityClass)
                    intentSetter?.invoke(intent)
                    startActivity(intent)
                    if (performFinish) {
                        this.finish()
                    }
                }
                else -> {
                }
            }
        }
    }

    protected fun withActivity(block: Activity.() -> Any) {
        activityNavigationEvent.postValue(SingleEvent(block))
    }

    protected fun withNavController(block: NavController.() -> Any) {
        fragmentNavigationEvent.postValue(SingleEvent(block))
    }

    open fun onBackPressed() {}
}