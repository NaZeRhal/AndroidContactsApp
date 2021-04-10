package com.maxrzhe.contactsapp.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxrzhe.contactsapp.adapters.ContactAdapter
import com.maxrzhe.contactsapp.databinding.FragmentContactListBinding
import com.maxrzhe.contactsapp.model.Contact
import com.maxrzhe.contactsapp.viewmodel.ContactViewModelFactory
import com.maxrzhe.contactsapp.viewmodel.SharedViewModel

class ContactListFragment : BaseViewBindingFragment<FragmentContactListBinding>() {
    private var contactAdapter: ContactAdapter? = null

    private val onSelectContactListener: OnSelectContactListener?
        get() = (context as? OnSelectContactListener)

    private val sharedViewModel: SharedViewModel by activityViewModels {
        ContactViewModelFactory(
            requireActivity().application
        )
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactListBinding =
        FragmentContactListBinding::inflate

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
        sharedViewModel.getContacts().observe(viewLifecycleOwner, { contacts ->
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