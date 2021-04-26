package com.maxrzhe.newcontactsapp.screens

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxrzhe.contacts.adapters.ContactAdapter
import com.maxrzhe.contacts.databinding.FragmentContactListBinding
import com.maxrzhe.contacts.viewmodel.SharedViewModel
import com.maxrzhe.core.screens.BaseFragment
import com.maxrzhe.newcontactsapp.viewmodel.ContactConsumerListViewModel
import com.maxrzhe.newcontactsapp.viewmodel.ContactConsumerViewModelFactory

class ContactConsumerListFragment :
    BaseFragment<FragmentContactListBinding, ContactConsumerListViewModel>() {
    private var contactAdapter: ContactAdapter? = null

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private val onSelectContactListener: OnSelectContactListener?
        get() = (context as? OnSelectContactListener)

    private val onInitFragmentViewListener: OnInitFragmentViewListener?
        get() = (context as? OnInitFragmentViewListener)

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
                            Log.i("PER_AL", "onClick: click")
                            sharedViewModel.select(contactId)
                            onSelectContactListener?.onSelect()
                        }
                    }
                )
                adapter = contactAdapter
                fabAdd.setOnClickListener(addContact())
            }
        }

        onInitFragmentViewListener?.run {
            Log.i("PER_AL", "initView: ${onInitFragmentView()}")
            if (onInitFragmentView()) {
                viewModel.findAll().observe(viewLifecycleOwner, { contacts ->
                    contactAdapter?.itemList = contacts
                })
            }
        }
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

    interface OnInitFragmentViewListener {
        fun onInitFragmentView(): Boolean
    }
}