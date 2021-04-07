package com.maxrzhe.contactsapp.screens

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

    private val onAddDetailListener: OnAddDetailListener?
        get() = (context as? OnAddDetailListener)


    companion object {
        private const val CONTACT_LIST = "contact_list"
        private const val SHARED_STORAGE_NAME = "storage"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactListBinding.inflate(inflater, container, false)

        return binding?.let { initView(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView(binding: FragmentContactListBinding): View {
        return with(binding) {
            rvContactList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                contactAdapter = ContactAdapter(
                    requireContext(),
                    object : ContactAdapter.OnContactClickListener {
                        override fun onClick(contact: Contact) {
                            onAddDetailListener?.onAddDetails(contact)
                        }
                    }
                )
                contactAdapter?.itemList = readContactsFromSharedPreferences()
                adapter = contactAdapter
                fabAdd.setOnClickListener(addContact())
            }
            root
        }
    }

    private fun addContact() = View.OnClickListener {
        onAddDetailListener?.onAddDetails(null)
    }

    private fun readContactsFromSharedPreferences(): List<Contact> {
        val sharedPreferences = context?.getSharedPreferences(
            SHARED_STORAGE_NAME,
            AppCompatActivity.MODE_PRIVATE
        )
        val savedContacts = sharedPreferences?.getString(CONTACT_LIST, null)
        val type = object : TypeToken<List<Contact>>() {}.type
        return Gson().fromJson<List<Contact>>(savedContacts, type) ?: emptyList()
    }

    private fun saveToSharedPreferences(contact: Contact) {
        val sharedPreferences = context?.getSharedPreferences(
            SHARED_STORAGE_NAME,
            AppCompatActivity.MODE_PRIVATE
        )
        sharedPreferences?.let {
            val savedJsonContacts =
                sharedPreferences.getString(CONTACT_LIST, null)
            val type = object : TypeToken<List<Contact>>() {}.type
            var savedContacts =
                Gson().fromJson<List<Contact>>(savedJsonContacts, type) ?: emptyList()

            val oldContact: Contact? = savedContacts.firstOrNull { it.id == contact.id }
            if (oldContact != null) {
                savedContacts = savedContacts - listOf(oldContact)
            }
            savedContacts = listOf(contact) + savedContacts
            val json = Gson().toJson(savedContacts)
            sharedPreferences.edit()?.putString(CONTACT_LIST, json)?.apply()
        }
    }

    fun saveContact(contact: Contact) {
        saveToSharedPreferences(contact)
        contactAdapter?.itemList = readContactsFromSharedPreferences()
        contactAdapter?.notifyDataSetChanged()
    }

    fun filter(newText: String?) {
        contactAdapter?.filter = newText
    }

    interface OnAddDetailListener {
        fun onAddDetails(contact: Contact?)
    }
}