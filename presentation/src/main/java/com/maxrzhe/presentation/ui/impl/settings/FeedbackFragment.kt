package com.maxrzhe.presentation.ui.impl.settings

import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.ui.base.CoreFragment
import com.maxrzhe.presentation.ui.impl.contacts.CoronavirusFragment

class FeedbackFragment : CoreFragment() {
    override fun layoutId(): Int = R.layout.fragment_feedback

    companion object {

        fun createInstance() = FeedbackFragment()
    }
}