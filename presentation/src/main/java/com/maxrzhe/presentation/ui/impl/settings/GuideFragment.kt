package com.maxrzhe.presentation.ui.impl.settings

import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.ui.base.CoreFragment
import com.maxrzhe.presentation.ui.impl.contacts.CoronavirusFragment

class GuideFragment : CoreFragment() {
    override fun layoutId(): Int = R.layout.fragment_guide

    companion object {

        fun createInstance() = GuideFragment()
    }
}