package com.maxrzhe.presentation.ui.impl.settings

import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.ui.base.CoreFragment

class ProfileFragment : CoreFragment() {
    override fun layoutId(): Int = R.layout.fragment_profile

    companion object {

        fun createInstance() = ProfileFragment()
    }
}