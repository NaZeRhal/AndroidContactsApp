package com.maxrzhe.presentation.ui.base

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

abstract class BaseFragmentWithBindingAndViewModel<VB : ViewBinding, VM : BaseViewModel> :
    BaseFragmentWithBinding<VB>() {
    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeNavigationEvent()
    }

    protected fun onBackPressed() {
        viewModel.onBackPressed()
        onReturnToPreviousScreen()
    }

    protected open fun onReturnToPreviousScreen() {
        findNavController().popBackStack()
    }

    private fun observeNavigationEvent() {
        viewModel.navigationEvent.observe(viewLifecycleOwner, { navEvent ->
            val consume: (NavController.() -> Any)? = navEvent?.consume()
            consume?.invoke(findNavController())
        })
    }
}