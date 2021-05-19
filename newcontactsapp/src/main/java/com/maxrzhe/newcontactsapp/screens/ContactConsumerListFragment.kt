package com.maxrzhe.newcontactsapp.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxrzhe.data.provider.ContactProviderHandler
import com.maxrzhe.newcontactsapp.viewmodel.ContactConsumerListViewModel
import com.maxrzhe.newcontactsapp.viewmodel.ContactConsumerViewModelFactory
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.adapters.ContactAdapter
import com.maxrzhe.presentation.databinding.FragmentContactListBinding
import com.maxrzhe.presentation.ui.base.BaseFragment
import com.maxrzhe.presentation.viewmodel.impl.SearchViewModel

class ContactConsumerListFragment :
    BaseFragment<FragmentContactListBinding, ContactConsumerListViewModel>(),
    ContactAdapter.OnSearchResultListener {
    private var contactAdapter: ContactAdapter? = null

    private val searchViewModel by activityViewModels<SearchViewModel>()

    override val viewModel by viewModels<ContactConsumerListViewModel> {
        ContactConsumerViewModelFactory(
            ContactProviderHandler(requireContext())
        )
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactListBinding =
        FragmentContactListBinding::inflate

    override fun bindView() {}

    override fun initView() {
        with(binding) {
            rvContactList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                contactAdapter = ContactAdapter(
                    object : ContactAdapter.OnContactClickListener {
                        override fun onClick(fbId: String) {
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
}