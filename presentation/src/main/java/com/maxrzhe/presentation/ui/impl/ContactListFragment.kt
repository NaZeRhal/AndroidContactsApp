package com.maxrzhe.presentation.ui.impl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.data_api.model.Contact
import com.google.android.material.snackbar.Snackbar
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.adapters.ContactsAdapter
import com.maxrzhe.presentation.adapters.GenericBindingAdapter
import com.maxrzhe.presentation.adapters.bindAdapter
import com.maxrzhe.presentation.databinding.FragmentContactListBinding
import com.maxrzhe.presentation.ui.SwipeToDeleteCallback
import com.maxrzhe.presentation.ui.base.BaseFragment
import com.maxrzhe.presentation.viewmodel.impl.ContactListViewModel
import com.maxrzhe.presentation.viewmodel.impl.SearchViewModel
import com.maxrzhe.presentation.viewmodel.impl.SharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ContactListFragment :
    BaseFragment<FragmentContactListBinding, ContactListViewModel>(),
    ContactsAdapter.OnSearchResultListener {

    companion object {
        private const val IS_FAVORITES = "is_favorites"

        fun createInstance(isFavorite: Boolean): ContactListFragment =
            ContactListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_FAVORITES, isFavorite)
                }
            }
    }

    private var contactsAdapter: ContactsAdapter? = null

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val searchViewModel by activityViewModels<SearchViewModel>()

    private val onSelectContactListener: OnSelectContactListener?
        get() = (context as? OnSelectContactListener)

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactListBinding =
        FragmentContactListBinding::inflate

    override val viewModel: ContactListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.isFavoritesPage = arguments?.getBoolean(IS_FAVORITES) ?: false
    }

    override fun bindView() {
        binding.listViewModel = viewModel
        binding.lifecycleOwner = requireActivity()
    }

    override fun initView() {
        contactsAdapter = ContactsAdapter()
        contactsAdapter?.addOnItemClickListener(object :
            GenericBindingAdapter.OnItemClickListener<Contact> {
            override fun onClickItem(data: Contact) {
                sharedViewModel.select(data.fbId)
                onSelectContactListener?.onSelect()
                binding.tvSearchResult.visibility = View.GONE
            }

        })
        contactsAdapter?.let {
            binding.rvContactList.bindAdapter(it)
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val contact = contactsAdapter?.getItemAt(position)
                contact?.let {
                    viewModel.delete(contact)
                }
            }
        }

        ItemTouchHelper(swipeToDeleteCallback).apply {
            attachToRecyclerView(binding.rvContactList)
        }
        contactsAdapter?.setOnSearchResultListener(this)

        viewModel.errorMessage.observe(viewLifecycleOwner,
            { msg ->
                if (msg != null) {
                    showErrorMessage(msg)
                }
            })

        searchViewModel.query.observe(viewLifecycleOwner,
            { query ->
                if (query != null) {
                    contactsAdapter?.filter = query
                }
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