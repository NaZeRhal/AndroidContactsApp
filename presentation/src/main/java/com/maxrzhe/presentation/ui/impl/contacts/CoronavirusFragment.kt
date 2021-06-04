package com.maxrzhe.presentation.ui.impl.contacts

import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.ui.base.CoreFragment

class CoronavirusFragment : CoreFragment() {
    override fun layoutId(): Int = R.layout.fragment_coronavirus

    companion object {

        fun createInstance() = CoronavirusFragment()
    }
}