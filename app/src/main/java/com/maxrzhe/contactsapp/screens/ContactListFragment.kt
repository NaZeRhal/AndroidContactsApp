package com.maxrzhe.contactsapp.screens

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maxrzhe.contactsapp.R
import com.maxrzhe.contactsapp.adapters.ContactAdapter
import com.maxrzhe.contactsapp.databinding.FragmentContactListBinding
import com.maxrzhe.contactsapp.model.Contact

class ContactListFragment : Fragment() {
    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding

    private var contactAdapter: ContactAdapter? = null
    private var contactToSave: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(CONTACTS_FRAGMENT_LISTENER_KEY) { _, bundle ->
            contactToSave = bundle.getParcelable(ContactDetailFragment.CONTACT_TO_SAVE)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var isLandscape = false
        arguments?.let {
            isLandscape = it.getBoolean(ContactsListActivity.IS_LANDSCAPE, false)
        }
        _binding = FragmentContactListBinding.inflate(inflater, container, false)

        return binding?.let {
            val view = it.root
            it.rvContactList.apply {
                layoutManager = LinearLayoutManager(view.context)
                setHasFixedSize(true)
                contactAdapter = ContactAdapter(
                    view.context,
                    object : ContactAdapter.OnContactClickListener {
                        override fun onClick(contact: Contact) {
                            val detailFragment = ContactDetailFragment().apply {
                                arguments = Bundle().apply {
                                    putParcelable(ContactDetailFragment.CONTACT, contact)
                                    putBoolean(ContactDetailFragment.IS_NEW_CONTACT, false)
                                    putBoolean(ContactsListActivity.IS_LANDSCAPE, isLandscape)
                                }
                            }
                            if (!isLandscape) {
                                parentFragmentManager.commit {
                                    replace(
                                        R.id.fl_container,
                                        detailFragment,
                                        ContactDetailFragment.DETAILS_FRAGMENT_TAG_KEY
                                    )
                                    setReorderingAllowed(true)
                                    addToBackStack(null)
                                }
                            } else {
                                parentFragmentManager.commit {
                                    replace(
                                        R.id.fl_details,
                                        detailFragment,
                                        ContactDetailFragment.DETAILS_FRAGMENT_TAG_KEY
                                    )
                                    setReorderingAllowed(true)
                                }
                            }
                        }
                    }
                )
                contactAdapter?.itemList = readContactsFromSharedPreferences()
                adapter = contactAdapter
                it.fabAdd.setOnClickListener(addContact(isLandscape))
            }
            view
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        (context as? ContactsListActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val searchView = (menu.findItem(R.id.menu_item_search)).actionView as SearchView
        val searchEditText = searchView.findViewById<EditText>(R.id.search_src_text)

        searchEditText.apply {
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.search_view_text_color
                )
            )

            setHintTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.search_view_hint_color
                )
            )
        }

        searchView.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            queryHint = resources.getString(R.string.toolbar_search_hint)

            setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        contactAdapter?.filter = newText
                        return true
                    }
                })
        }
    }

//    override fun onStart() {
//        super.onStart()
//        Log.i(ContactsListActivity.TAG, "onStart: list")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Log.i(ContactsListActivity.TAG, "onResume: list")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.i(ContactsListActivity.TAG, "onPause: list")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.i(ContactsListActivity.TAG, "onStop: list")
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        Log.i(ContactsListActivity.TAG, "onDestroy: list")
//    }

    private fun addContact(isLandscape: Boolean) = View.OnClickListener {
        parentFragmentManager.commit {
            val detailFragment = ContactDetailFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ContactDetailFragment.IS_NEW_CONTACT, true)
                    putBoolean(ContactsListActivity.IS_LANDSCAPE, isLandscape)
                }
            }
            if (!isLandscape) {
                replace(
                    R.id.fl_container,
                    detailFragment,
                    ContactDetailFragment.DETAILS_FRAGMENT_TAG_KEY
                )
                setReorderingAllowed(true)
                addToBackStack(null)
            } else {
                replace(
                    R.id.fl_details,
                    detailFragment,
                    ContactDetailFragment.DETAILS_FRAGMENT_TAG_KEY
                )
                setReorderingAllowed(true)
            }
        }
    }

    private fun readContactsFromSharedPreferences(): List<Contact> {
        val sharedPreferences = context?.getSharedPreferences(
            ContactsListActivity.SHARED_STORAGE_NAME,
            AppCompatActivity.MODE_PRIVATE
        )
        val savedContacts = sharedPreferences?.getString(ContactsListActivity.CONTACT_LIST, null)
        val type = object : TypeToken<List<Contact>>() {}.type
        return Gson().fromJson<List<Contact>>(savedContacts, type) ?: emptyList()
    }

    companion object {
        const val CONTACTS_FRAGMENT_LISTENER_KEY = "contact_fragment_listener_key"
        const val CONTACTS_FRAGMENT_TAG_KEY = "CONTACTS_FRAGMENT_TAG_KEY"
    }

    interface OnContactSaveListener {
        fun onSave()
    }
}