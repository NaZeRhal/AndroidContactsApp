package com.maxrzhe.presentation.ui.impl.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.maxrzhe.presentation.adapters.BaseBindingAdapter
import com.maxrzhe.presentation.adapters.bindAdapter
import com.maxrzhe.presentation.databinding.FragmentContactListBinding
import com.maxrzhe.presentation.databinding.ItemContactBinding
import com.maxrzhe.presentation.model.ContactItemViewModel
import com.maxrzhe.presentation.ui.SwipeToDeleteCallback
import com.maxrzhe.presentation.ui.base.BaseFragmentWithViewModel
import com.maxrzhe.presentation.viewmodel.impl.contacts.ContactListViewModel
import com.maxrzhe.presentation.viewmodel.impl.contacts.SearchViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ContactListFragment :
    BaseFragmentWithViewModel<FragmentContactListBinding, ContactListViewModel>() {

    companion object {
        private const val IS_FAVORITES = "is_favorites"
        const val FB_ID_CONTACT = "fb_id_contact"

        fun createInstance(isFavorite: Boolean): ContactListFragment =
            ContactListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_FAVORITES, isFavorite)
                }
            }
    }

    private var contactsAdapter: BaseBindingAdapter<ItemContactBinding>? = null

    private val searchViewModel: SearchViewModel by sharedViewModel()

    override val viewModel: ContactListViewModel by viewModel()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContactListBinding =
        FragmentContactListBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.isFavoritesPage = arguments?.getBoolean(IS_FAVORITES) ?: false
    }

    override fun bindView() {
        binding.listViewModel = viewModel
        binding.lifecycleOwner = requireActivity()
    }

    override fun initView() {
        contactsAdapter = BaseBindingAdapter()

        contactsAdapter?.let {
            binding.rvContactList.bindAdapter(it)
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val contactItem = contactsAdapter?.getItemAt(position) as? ContactItemViewModel
                contactItem?.let {
                    viewModel.delete(contactItem)
                }
            }
        }

        ItemTouchHelper(swipeToDeleteCallback).apply {
            attachToRecyclerView(binding.rvContactList)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner, { msg ->
            if (msg != null) {
                showErrorMessage(msg)
            }
        })

        searchViewModel.query.observe(viewLifecycleOwner, { query ->
            viewModel.searchQuery.set(query)
        })
    }


    private fun showErrorMessage(msg: String) {
        view?.let {
            Snackbar.make(it, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
            }.show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.performFiltering()
    }
}