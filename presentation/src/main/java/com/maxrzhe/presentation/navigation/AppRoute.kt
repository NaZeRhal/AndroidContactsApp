package com.maxrzhe.presentation.navigation

import androidx.annotation.IdRes
import com.maxrzhe.presentation.R

sealed class RouteSection(@IdRes val graph: Int) {
    object Settings : RouteSection(R.id.settings_nav)
}

sealed class RouteDestination(@IdRes val destination: Int) {
    object Back : RouteDestination(-1)

    sealed class Settings(@IdRes destination: Int) : RouteDestination(destination) {

        object Profile : Settings(R.id.profileFragment)
        object Guide : Settings(R.id.guideFragment)
        object Feedback : Settings(R.id.feedbackFragment)
    }
}