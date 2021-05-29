package com.maxrzhe.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.maxrzhe.presentation.databinding.FragmentProfileBinding
import com.maxrzhe.presentation.ui.base.CoreFragment

class ProfileFragment : CoreFragment<FragmentProfileBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding
        get() = FragmentProfileBinding::inflate

    override fun initView() {}

    override fun bindView() {}

}