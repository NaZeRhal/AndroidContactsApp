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
import com.google.android.material.snackbar.Snackbar
import com.maxrzhe.contacts.R
import com.maxrzhe.contacts.adapters.ContactAdapter
import com.maxrzhe.contacts.databinding.FragmentContactListBinding
import com.maxrzhe.contacts.remote.Resource
import com.maxrzhe.contacts.viewmodel.BaseViewModelFactory
import com.maxrzhe.contacts.viewmodel.ContactListViewModel
import com.maxrzhe.contacts.viewmodel.SearchViewModel
import com.maxrzhe.contacts.viewmodel.SharedViewModel
import com.maxrzhe.core.screens.BaseFragment

class ContactListFragment :
    BaseFragment<FragmentContactListBinding, ContactListViewModel>(),
    ContactAdapter.OnSearchResultListener {

    companion object {
        const val IS_FAVORITES = "is_favorites"
    }

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

    override fun bindView() {
        binding.listViewModel = viewModel
        binding.lifecycleOwner = requireActivity()
    }

    override fun initView() {
        with(binding) {
            rvContactList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                contactAdapter = ContactAdapter(
                    requireContext(),
                    object : ContactAdapter.OnContactClickListener {
                        override fun onClick(fbId: String) {
                            sharedViewModel.select(fbId)
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
        contactAdapter?.setOnSearchResultListener(this)

        subscribeUi()

        searchViewModel.query.observe(viewLifecycleOwner, { query ->
            contactAdapter?.filter = query
        })
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

    private fun subscribeUi() {
        viewModel.isFavorites = isFavorites
        viewModel.allContacts.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        contactAdapter?.itemList = it
                    }
                    viewModel.isLoading.set(false)
                }
                is Resource.Loading -> {
                    viewModel.isLoading.set(true)
                }
                is Resource.Error -> {
                    result.data?.let {
                        contactAdapter?.itemList = it
                    }
                    result.error?.message?.let {
                        showErrorMessage(it)
                    }
                    viewModel.isLoading.set(false)
                }
            }
        })
    }

    private fun showErrorMessage(msg: String) {
        view?.let {
            Snackbar.make(it, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
            }.show()
        }
    }

    interface OnSelectContactListener {
        fun onSelect()
    }
}