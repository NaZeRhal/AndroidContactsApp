package com.maxrzhe.contactsapp.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maxrzhe.contactsapp.adapters.ContactAdapter
import com.maxrzhe.contactsapp.databinding.FragmentContactListBinding
import com.maxrzhe.contactsapp.model.Contact

class ContactListFragment : Fragment() {
    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding

    private var contactAdapter: ContactAdapter? = null
    private var onAddDetailListener: OnAddDetailListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnAddDetailListener) {
            onAddDetailListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        onAddDetailListener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                            onAddDetailListener?.onAddDetails(contact)
                        }
                    }
                )
                contactAdapter?.itemList = readContactsFromSharedPreferences()
                adapter = contactAdapter
                it.fabAdd.setOnClickListener(addContact())
            }
            view
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addContact() = View.OnClickListener {
        onAddDetailListener?.onAddDetails(null)
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

    private fun saveToSharedPreferences(contact: Contact) {
        val sharedPreferences = context?.getSharedPreferences(
            ContactsListActivity.SHARED_STORAGE_NAME,
            AppCompatActivity.MODE_PRIVATE
        )
        sharedPreferences?.let {
            val savedJsonContacts =
                sharedPreferences.getString(ContactsListActivity.CONTACT_LIST, null)
            val type = object : TypeToken<List<Contact>>() {}.type
            var savedContacts =
                Gson().fromJson<List<Contact>>(savedJsonContacts, type) ?: emptyList()

            val oldContact: Contact? = savedContacts.firstOrNull { it.id == contact.id }
            if (oldContact != null) {
                savedContacts = savedContacts - listOf(oldContact)
            }
            savedContacts = listOf(contact) + savedContacts
            val json = Gson().toJson(savedContacts)
            sharedPreferences.edit()?.putString(ContactsListActivity.CONTACT_LIST, json)?.apply()
        }
    }

    fun saveContact(contact: Contact) {
        saveToSharedPreferences(contact)
        contactAdapter?.itemList = readContactsFromSharedPreferences()
    }

    fun filter(newText: String?) {
        contactAdapter?.filter = newText
    }

    companion object {
        const val TAG_KEY = "CONTACTS_FRAGMENT_TAG_KEY"
    }

    interface OnAddDetailListener {
        fun onAddDetails(contact: Contact?)
    }
}