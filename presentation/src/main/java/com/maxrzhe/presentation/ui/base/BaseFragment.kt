package com.maxrzhe.presentation.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    protected abstract val viewModel: VM

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    protected abstract fun initView()
    protected abstract fun bindView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = bindingInflater(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}