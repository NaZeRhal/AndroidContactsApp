package com.maxrzhe.newcontactsapp.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxrzhe.contacts.adapters.ContactAdapter
import com.maxrzhe.contacts.databinding.FragmentContactListBinding
import com.maxrzhe.core.screens.BaseFragment
import com.maxrzhe.newcontactsapp.viewmodel.ContactConsumerListViewModel
import com.maxrzhe.newcontactsapp.viewmodel.ContactConsumerViewModelFactory

class ContactConsumerListFragment :
    BaseFragment<FragmentContactListBinding, ContactConsumerListViewModel>() {
    private var contactAdapter: ContactAdapter? = null

    override val viewModelFactory: ViewModelProvider.Factory
        get() = ContactConsumerViewModelFactory(requireActivity().application)

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactListBinding =
        FragmentContactListBinding::inflate

    override fun getViewModelClass() = ContactConsumerListViewModel::class.java

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
                            //NOTHING TO DO
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
}