package com.maxrzhe.contacts.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxrzhe.contacts.adapters.ContactAdapter
import com.maxrzhe.contacts.databinding.FragmentContactListBinding
import com.maxrzhe.contacts.viewmodel.BaseViewModelFactory
import com.maxrzhe.contacts.viewmodel.ContactListViewModel
import com.maxrzhe.contacts.viewmodel.SharedViewModel
import com.maxrzhe.core.screens.BaseFragment

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