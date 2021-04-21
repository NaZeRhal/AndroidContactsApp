package com.maxrzhe.contactsfriendapp.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.maxrzhe.contactsfriendapp.databinding.FragmentContactDetailBinding
import com.maxrzhe.contactsfriendapp.viewmodel.BaseViewModelFactory
import com.maxrzhe.contactsfriendapp.viewmodel.ContactDetailViewModel
import com.maxrzhe.contactsfriendapp.viewmodel.SharedViewModel

class ContactDetailFragment :
    BaseFragment<FragmentContactDetailBinding, ContactDetailViewModel>() {

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override val viewModelFactory: ViewModelProvider.Factory
        get() = BaseViewModelFactory(requireActivity().application)

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactDetailBinding =
        FragmentContactDetailBinding::inflate

    override fun getViewModelClass() = ContactDetailViewModel::class.java

    override fun bindView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
    }

    override fun initView() {
        sharedViewModel.contactId.observe(viewLifecycleOwner, {
            viewModel.manageSelectedId(it)
        })
    }
}