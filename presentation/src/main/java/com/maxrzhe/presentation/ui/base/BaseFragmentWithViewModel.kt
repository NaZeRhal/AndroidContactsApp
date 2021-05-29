package com.maxrzhe.presentation.ui.base

import androidx.viewbinding.ViewBinding
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

abstract class BaseFragmentWithViewModel<VB : ViewBinding, VM : BaseViewModel> : CoreFragment<VB>() {
    protected abstract val viewModel: VM
}