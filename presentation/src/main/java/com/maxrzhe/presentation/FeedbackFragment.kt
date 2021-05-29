package com.maxrzhe.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.maxrzhe.presentation.databinding.FragmentFeedbackBinding
import com.maxrzhe.presentation.ui.base.CoreFragment

class FeedbackFragment : CoreFragment<FragmentFeedbackBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFeedbackBinding
        get() = FragmentFeedbackBinding::inflate

    override fun initView() {}

    override fun bindView() {}
}