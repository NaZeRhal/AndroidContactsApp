package com.maxrzhe.contactsapp.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxrzhe.contactsapp.adapters.ContactAdapter
import com.maxrzhe.contactsapp.databinding.FragmentContactListBinding
import com.maxrzhe.contactsapp.model.Contact
import com.maxrzhe.contactsapp.viewmodel.SharedViewModel
import com.maxrzhe.contactsapp.viewmodel.SharedViewModelFactory

class ContactListFragment :
    BaseFragment<FragmentContactListBinding, SharedViewModel>() {
    private var contactAdapter: ContactAdapter? = null

    private val onSelectContactListener: OnSelectContactListener?
        get() = (context as? OnSelectContactListener)

    override val viewModelFactory: ViewModelProvider.Factory
        get() = SharedViewModelFactory(requireActivity().application)

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactListBinding =
        FragmentContactListBinding::inflate

    override fun getViewModelClass() = SharedViewModel::class.java

    override fun setup() {
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
        sharedViewModel.findAll().observe(viewLifecycleOwner, { contacts ->
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