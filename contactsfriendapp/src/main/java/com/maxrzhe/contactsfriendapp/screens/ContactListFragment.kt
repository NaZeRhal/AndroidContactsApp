package com.maxrzhe.contactsfriendapp.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxrzhe.contactsfriendapp.adapters.ContactAdapter
import com.maxrzhe.contactsfriendapp.databinding.FragmentContactListBinding
import com.maxrzhe.contactsfriendapp.viewmodel.BaseViewModelFactory
import com.maxrzhe.contactsfriendapp.viewmodel.ContactListViewModel
import com.maxrzhe.contactsfriendapp.viewmodel.SharedViewModel

class ContactListFragment :
    BaseFragment<FragmentContactListBinding, ContactListViewModel>() {
    private var contactAdapter: ContactAdapter? = null

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private val onSelectContactListener: OnSelectContactListener?
        get() = (context as? OnSelectContactListener)

    override val viewModelFactory: ViewModelProvider.Factory
        get() = BaseViewModelFactory(requireActivity().application)

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactListBinding =
        FragmentContactListBinding::inflate

    override fun getViewModelClass() = ContactListViewModel::class.java

    override fun bindView() {}

    override fun initView() {
        with(binding) {
            rvContactList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                contactAdapter = ContactAdapter(
                    requireContext(),
                    object : ContactAdapter.OnContactClickListener {
                        override fun onClick(contactId: Long) {
                            sharedViewModel.select(contactId)
                            onSelectContactListener?.onSelect()
                        }
                    }
                )
                adapter = contactAdapter
            }
        }
        viewModel.findAll().observe(viewLifecycleOwner, { contacts ->
            contactAdapter?.itemList = contacts
        })
    }

    fun filter(newText: String?) {
        contactAdapter?.filter = newText
    }

    interface OnSelectContactListener {
        fun onSelect()
    }
}