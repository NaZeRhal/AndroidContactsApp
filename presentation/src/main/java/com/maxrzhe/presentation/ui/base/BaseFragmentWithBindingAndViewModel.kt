package com.maxrzhe.presentation.ui.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.maxrzhe.presentation.navigation.listenToRouter
import com.maxrzhe.presentation.viewmodel.base.ViewModelWithRouter

abstract class BaseFragmentWithBindingAndViewModel<VB : ViewBinding, VM : ViewModelWithRouter> :
    BaseFragmentWithBinding<VB>() {
    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onReturnToPreviousScreen()
                }
            })
        listenToRouter(viewModel.router)
    }

    protected open fun onReturnToPreviousScreen() {
        findNavController().popBackStack()
    }
}