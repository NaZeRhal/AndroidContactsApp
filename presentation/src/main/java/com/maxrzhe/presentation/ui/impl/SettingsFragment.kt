package com.maxrzhe.presentation.ui.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.adapters.BaseBindingAdapter
import com.maxrzhe.presentation.adapters.bindAdapter
import com.maxrzhe.presentation.databinding.FragmentSettingsBinding
import com.maxrzhe.presentation.databinding.ItemSettingsBinding
import com.maxrzhe.presentation.ui.base.BaseFragmentWithViewModel
import com.maxrzhe.presentation.viewmodel.impl.SettingsListViewModel

class SettingsFragment : BaseFragmentWithViewModel<FragmentSettingsBinding, SettingsListViewModel>() {

    override val viewModel: SettingsListViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding
        get() = FragmentSettingsBinding::inflate

    override fun initView() {
        val settingsAdapter: BaseBindingAdapter<ItemSettingsBinding> = BaseBindingAdapter()
        binding.rvSettingsList.bindAdapter(settingsAdapter)
    }

    override fun bindView() {
        binding.settingsViewModel = viewModel
        binding.lifecycleOwner = requireActivity()
    }
}