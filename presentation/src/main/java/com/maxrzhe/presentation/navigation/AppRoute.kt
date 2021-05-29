package com.maxrzhe.presentation.navigation

import androidx.annotation.IdRes
import com.maxrzhe.presentation.R

sealed class RouteSection(@IdRes val graph: Int) {
    object Contacts : RouteSection(R.id.contacts_nav)
    object Settings : RouteSection(R.id.settings_nav)
}

sealed class RouteDestination(@IdRes val destination: Int) {
    object Back : RouteDestination(-1)

    sealed class Settings(@IdRes destination: Int) : RouteDestination(destination) {

        object Profile : Settings(R.id.profileFragment)
        object Guide : Settings(R.id.guideFragment)
        object Feedback : Settings(R.id.feedbackFragment)
    }

    sealed class Contacts(@IdRes destination: Int) : RouteDestination(destination) {

        object Splash : Contacts(R.id.splashFragment)
        object HomeViewPager : Contacts(R.id.homeFragment)
        object Detail: Contacts(R.id.contactDetailFragment)
    }
}