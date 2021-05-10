package com.maxrzhe.newcontactsapp.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxrzhe.contacts.R
import com.maxrzhe.contacts.adapters.ContactAdapter
import com.maxrzhe.contacts.databinding.FragmentContactListBinding
import com.maxrzhe.contacts.viewmodel.SearchViewModel
import com.maxrzhe.core.screens.BaseFragment
import com.maxrzhe.newcontactsapp.viewmodel.ContactConsumerListViewModel
import com.maxrzhe.newcontactsapp.viewmodel.ContactConsumerViewModelFactory

class ContactConsumerListFragment :
    BaseFragment<FragmentContactListBinding, ContactConsumerListViewModel>(), ContactAdapter.OnSearchResultListener {
    private var contactAdapter: ContactAdapter? = null

    override val viewModelFactory: ViewModelProvider.Factory
        get() = ContactConsumerViewModelFactory(requireActivity().application)

    private val searchViewModel by activityViewModels<SearchViewModel>()

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