package com.maxrzhe.presentation.ui.impl.settings

import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.adapters.BaseBindingAdapter
import com.maxrzhe.presentation.adapters.bindAdapter
import com.maxrzhe.presentation.databinding.FragmentSettingsBinding
import com.maxrzhe.presentation.databinding.ItemSettingsBinding
import com.maxrzhe.presentation.ui.base.BaseFragmentWithBindingAndViewModel
import com.maxrzhe.presentation.viewmodel.impl.settings.SettingsListViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsFragment :
    BaseFragmentWithBindingAndViewModel<FragmentSettingsBinding, SettingsListViewModel>() {

    override val viewModel: SettingsListViewModel by viewModel()

    override fun initView() {
        val settingsAdapter: BaseBindingAdapter<ItemSettingsBinding> = BaseBindingAdapter()
        binding.rvSettingsList.bindAdapter(settingsAdapter)
    }

    override fun layoutId(): Int = R.layout.fragment_settings

    override fun bindView() {
        binding.settingsViewModel = viewModel
        binding.lifecycleOwner = requireActivity()
    }

    override fun onReturnToPreviousScreen() {
        viewModel.openContactsSection()
    }

    companion object {

        fun createInstance(): SettingsFragment = SettingsFragment()
    }
}