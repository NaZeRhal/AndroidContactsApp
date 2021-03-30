package com.maxrzhe.contactsapp.screens

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maxrzhe.contactsapp.R
import com.maxrzhe.contactsapp.adapters.ContactAdapter
import com.maxrzhe.contactsapp.databinding.ActivityListContactsBinding
import com.maxrzhe.contactsapp.model.Contact


class ContactsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListContactsBinding

    private var contactAdapter: ContactAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListContactsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.tbMain)
        title = null

        val savedContacts = readContactsFromSharedPreferences()

        binding.rvContacts.apply {
            layoutManager = LinearLayoutManager(this@ContactsListActivity)
            setHasFixedSize(true)
            contactAdapter = ContactAdapter(
                this@ContactsListActivity,
                object : ContactAdapter.OnContactClickListener {
                    override fun onClick(position: Int, contact: Contact) {
                        //go to info activity
                    }
                })
            contactAdapter?.itemList = savedContacts
            adapter = contactAdapter
        }

        binding.fabAdd.setOnClickListener {
            startActivityForResult(
                Intent(this, AddDetailActivity::class.java),
                DETAIL_ACTIVITY_REQUEST_CODE
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchView = (menu.findItem(R.id.menu_item_search)).actionView as SearchView
        val searchEditText = searchView.findViewById<EditText>(R.id.search_src_text)

        searchEditText.apply {
            setTextColor(
                ContextCompat.getColor(
                    this@ContactsListActivity,
                    R.color.search_view_text_color
                )
            )

            setHintTextColor(
                ContextCompat.getColor(
                    this@ContactsListActivity,
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
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DETAIL_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val contact = data?.extras?.get(AddDetailActivity.NEW_CONTACT) as Contact
                contactAdapter?.addContact(contact)
                binding.rvContacts.scrollToPosition(0)
            }
        }
    }

    private fun readContactsFromSharedPreferences(): ArrayList<Contact> {
        val sp = this.getSharedPreferences(SHARED_STORAGE_NAME, MODE_PRIVATE)
        val savedContacts = sp.getString(CONTACT_LIST, null)
        val type = object : TypeToken<ArrayList<Contact>>() {}.type
        return Gson().fromJson<ArrayList<Contact>>(savedContacts, type) ?: ArrayList()
    }

    companion object {
        const val DETAIL_ACTIVITY_REQUEST_CODE = 111

        const val SHARED_STORAGE_NAME = "storage"
        const val CONTACT_LIST = "contact_list"
    }

}