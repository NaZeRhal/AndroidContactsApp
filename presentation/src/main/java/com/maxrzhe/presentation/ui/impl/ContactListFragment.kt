package com.maxrzhe.presentation.ui.impl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.adapters.ContactAdapter
import com.maxrzhe.presentation.adapters.bindAdapter
import com.maxrzhe.presentation.databinding.FragmentContactListBinding
import com.maxrzhe.presentation.ui.SwipeToDeleteCallback
import com.maxrzhe.presentation.ui.base.BaseFragment
import com.maxrzhe.presentation.viewmodel.impl.ContactListViewModel
import com.maxrzhe.presentation.viewmodel.impl.SearchViewModel
import com.maxrzhe.presentation.viewmodel.impl.SharedViewModel
import kotlin.reflect.KClass

class ContactListFragment :
    BaseFragment<FragmentContactListBinding, ContactListViewModel>(),
    ContactAdapter.OnSearchResultListener {

    companion object {
        private const val IS_FAVORITES = "is_favorites"

        fun createInstance(isFavorite: Boolean): ContactListFragment =
            ContactListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_FAVORITES, isFavorite)
                }
            }
    }

    private var contactAdapter: ContactAdapter? = null

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val searchViewModel by activityViewModels<SearchViewModel>()

    private val onSelectContactListener: OnSelectContactListener?
        get() = (context as? OnSelectContactListener)

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactListBinding =
        FragmentContactListBinding::inflate

    override val viewModelClass: KClass<ContactListViewModel> = ContactListViewModel::class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.isFavoritesPage = arguments?.getBoolean(IS_FAVORITES) ?: false
    }

    override fun bindView() {
        binding.listViewModel = viewModel
        binding.lifecycleOwner = requireActivity()
    }

    override fun initView() {
        contactAdapter = ContactAdapter(
            object : ContactAdapter.OnContactClickListener {
                override fun onClick(fbId: String) {
                    sharedViewModel.select(fbId)
                    onSelectContactListener?.onSelect()
                    binding.tvSearchResult.visibility = View.GONE
                }
            }
        )
        binding.rvContactList.bindAdapter(contactAdapter)

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
            attachToRecyclerView(binding.rvContactList)
        }
        contactAdapter?.setOnSearchResultListener(this)

        viewModel.errorMessage.observe(viewLifecycleOwner,
            { msg ->
                if (msg != null) {
                    showErrorMessage(msg)
                }
            })

        searchViewModel.query.observe(viewLifecycleOwner,
            { query ->
                if (query != null) {
                    contactAdapter?.filter = query
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