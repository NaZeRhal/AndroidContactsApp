package com.maxrzhe.presentation.ui.impl.settings

import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.ui.base.CoreFragment
import com.maxrzhe.presentation.ui.impl.contacts.CoronavirusFragment

class ProfileFragment : CoreFragment() {
    override fun layoutId(): Int = R.layout.fragment_profile

    companion object {

        fun createInstance() = ProfileFragment()
    }
}