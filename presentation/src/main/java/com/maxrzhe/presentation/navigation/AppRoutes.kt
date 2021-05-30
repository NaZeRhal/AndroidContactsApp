package com.maxrzhe.presentation.navigation

import androidx.annotation.IdRes
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.ui.ContactsListActivity
import com.maxrzhe.presentation.ui.SettingsActivity
import com.maxrzhe.presentation.ui.VolumeSettingActivity

sealed class RouteSection(val activityClass: Class<*>?) {
    object ExitApp : RouteSection(null)
    object Contacts : RouteSection(ContactsListActivity::class.java)
    object Settings : RouteSection(SettingsActivity::class.java)
    object VolumeSetting : RouteSection(VolumeSettingActivity::class.java)
}

sealed class RouteFragmentDestination(@IdRes val destination: Int) {
    object Back : RouteFragmentDestination(-1)

    sealed class Settings(@IdRes destination: Int) : RouteFragmentDestination(destination) {

        object Profile : Settings(R.id.profileFragment)
        object Guide : Settings(R.id.guideFragment)
        object Feedback : Settings(R.id.feedbackFragment)
    }

    sealed class Contacts(@IdRes destination: Int) : RouteFragmentDestination(destination) {

        object HomeViewPager : Contacts(R.id.homeFragment)
        object Detail : Contacts(R.id.contactDetailFragment)
    }
}