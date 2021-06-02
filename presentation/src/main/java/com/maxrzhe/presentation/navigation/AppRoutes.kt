package com.maxrzhe.presentation.navigation

import androidx.navigation.NavDirections
import com.maxrzhe.presentation.ui.ContactsListActivity
import com.maxrzhe.presentation.ui.SettingsActivity
import com.maxrzhe.presentation.ui.VolumeSettingActivity
import com.maxrzhe.presentation.ui.impl.contacts.ContactDetailFragmentDirections
import com.maxrzhe.presentation.ui.impl.contacts.HomeFragmentDirections
import com.maxrzhe.presentation.ui.impl.settings.FeedbackFragmentDirections
import com.maxrzhe.presentation.ui.impl.settings.GuideFragmentDirections
import com.maxrzhe.presentation.ui.impl.settings.ProfileFragmentDirections
import com.maxrzhe.presentation.ui.impl.settings.SettingsFragmentDirections

sealed class RouteSection(val activityClass: Class<*>?) {
    object ExitApp : RouteSection(null)
    object Contacts : RouteSection(ContactsListActivity::class.java)
    object Settings : RouteSection(SettingsActivity::class.java)
    object VolumeSetting : RouteSection(VolumeSettingActivity::class.java)
}

sealed class RouteFragmentDestination {
    abstract val direction: NavDirections?

    object Back : RouteFragmentDestination() {
        override val direction: NavDirections? = null
    }

    sealed class Settings : RouteFragmentDestination() {

        object ToProfile : Settings() {
            override val direction: NavDirections =
                SettingsFragmentDirections.navigateToProfileFragment()
        }

        object FromProfile : Settings() {
            override val direction: NavDirections =
                ProfileFragmentDirections.navigateProfileToSettingsFragment()
        }

        object ToGuide : Settings() {
            override val direction: NavDirections =
                SettingsFragmentDirections.navigateToGuideFragment()
        }

        object FromGuide : Settings() {
            override val direction: NavDirections =
                GuideFragmentDirections.navigateGuideToSettingsFragment()
        }

        object ToFeedback : Settings() {
            override val direction: NavDirections =
                SettingsFragmentDirections.navigateToFeedbackFragment()
        }

        object FromFeedback : Settings() {
            override val direction: NavDirections =
                FeedbackFragmentDirections.navigateFeedbackToSettingsFragment()
        }
    }

    sealed class Contacts : RouteFragmentDestination() {

        object ToHomeViewPager : Contacts() {
            override val direction: NavDirections =
                ContactDetailFragmentDirections.navigateToHomeFragment()
        }

        class ToDetail(fbId: String? = null) : Contacts() {
            override val direction: NavDirections =
                HomeFragmentDirections.navigateToDetailFragment(fbId)
        }
    }
}

