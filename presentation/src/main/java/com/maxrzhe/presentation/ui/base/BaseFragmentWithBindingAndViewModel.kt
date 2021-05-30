package com.maxrzhe.presentation.ui.base

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.maxrzhe.presentation.navigation.listenToRouter
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

abstract class BaseFragmentWithBindingAndViewModel<VB : ViewBinding, VM : BaseViewModel> :
    BaseFragmentWithBinding<VB>() {
    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listenToRouter(viewModel.router)

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onReturnToPreviousScreen()
                }
            })
    }

    protected open fun onReturnToPreviousScreen() {
        findNavController().popBackStack()
    }
}