package com.maxrzhe.presentation.ui.impl.settings

import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.ui.base.CoreFragment

class GuideFragment : CoreFragment() {
    override fun layoutId(): Int = R.layout.fragment_guide

    companion object {

        fun createInstance() = GuideFragment()
    }
}