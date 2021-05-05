package com.maxrzhe.contacts.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maxrzhe.contacts.R
import com.maxrzhe.contacts.adapters.ContactAdapter
import com.maxrzhe.contacts.app.ContactsApp
import com.maxrzhe.contacts.databinding.FragmentContactListBinding
import com.maxrzhe.contacts.viewmodel.BaseViewModelFactory
import com.maxrzhe.contacts.viewmodel.ContactListViewModel
import com.maxrzhe.contacts.viewmodel.SearchViewModel
import com.maxrzhe.contacts.viewmodel.SharedViewModel
import com.maxrzhe.core.screens.BaseFragment

class ContactListFragment :
    BaseFragment<FragmentContactListBinding, ContactListViewModel>(),
    ContactAdapter.OnSearchResultListener {
    private var contactAdapter: ContactAdapter? = null
    private var isFavorites = false

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val searchViewModel by activityViewModels<SearchViewModel>()

    private val onSelectContactListener: OnSelectContactListener?
        get() = (context as? OnSelectContactListener)

    override val viewModelFactory: ViewModelProvider.Factory
        get() = BaseViewModelFactory(requireActivity().application)

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactListBinding =
        FragmentContactListBinding::inflate

    override fun getViewModelClass() = ContactListViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isFavorites = it.getBoolean(IS_FAVORITES)
        }
    }

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
                            binding.tvSearchResult.visibility = View.GONE
                        }
                    }
                )
                adapter = contactAdapter
            }

            val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    contactAdapter?.let {
                        val contact = it.getContactAt(position)
                        viewModel.delete(contact)
                    }
                }
            }

            ItemTouchHelper(swipeToDeleteCallback).apply {
                attachToRecyclerView(rvContactList)
            }
        }
        viewModel.isFavorites = isFavorites
        viewModel.findAll().observe(viewLifecycleOwner, { contacts ->
            contactAdapter?.itemList = contacts
        })

        contactAdapter?.setOnSearchResultListener(this)
        searchViewModel.query.observe(viewLifecycleOwner, { query ->
            contactAdapter?.filter = query
        })
    }

    interface OnSelectContactListener {
        fun onSelect()
    }

    companion object {
        const val IS_FAVORITES = "is_favorites"
    }

    override fun onSearchResult(resultCount: Int) {
        if (resultCount >= 0) {
            binding.tvSearchResult.visibility = View.VISIBLE
            val result =
                resources.getQuantityString(
                    R.plurals.search_result_plurals,
                    resultCount,
                    resultCount
                )
            binding.tvSearchResult.text = result
        } else {
            binding.tvSearchResult.visibility = View.GONE
            binding.tvSearchResult.text = ""
        }
    }
}