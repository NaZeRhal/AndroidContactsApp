package com.maxrzhe.contactsapp.screens

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxrzhe.contactsapp.adapters.ContactAdapter
import com.maxrzhe.contactsapp.databinding.FragmentContactListBinding
import com.maxrzhe.contactsapp.model.Contact
import com.maxrzhe.contactsapp.viewmodel.BaseViewModelFactory
import com.maxrzhe.contactsapp.viewmodel.ContactListViewModel
import com.maxrzhe.contactsapp.viewmodel.SharedViewModel

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
                        override fun onClick(contact: Contact) {
                            sharedViewModel.select(contact)
                            onSelectContactListener?.onSelect()
                        }
                    }
                )
                adapter = contactAdapter
                fabAdd.setOnClickListener(addContact())
            }
        }
        viewModel.findAll().observe(viewLifecycleOwner, { contacts ->
            contactAdapter?.itemList = contacts
        })
    }

    private fun addContact() = View.OnClickListener {
        sharedViewModel.select(null)
        onSelectContactListener?.onSelect()
    }

    fun filter(newText: String?) {
        contactAdapter?.filter = newText
    }

    interface OnSelectContactListener {
        fun onSelect()
    }
}