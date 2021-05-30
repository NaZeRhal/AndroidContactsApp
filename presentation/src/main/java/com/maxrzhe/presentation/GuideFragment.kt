package com.maxrzhe.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.maxrzhe.presentation.databinding.FragmentGuideBinding
import com.maxrzhe.presentation.ui.base.BaseFragmentWithBinding

class GuideFragment : BaseFragmentWithBinding<FragmentGuideBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGuideBinding
        get() = FragmentGuideBinding::inflate

    override fun initView() {}

    override fun bindView() {}

}