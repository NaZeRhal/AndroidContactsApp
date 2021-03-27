package com.maxrzhe.contactsapp.screens

import android.os.Bundle
import android.view.Menu
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxrzhe.contactsapp.R
import com.maxrzhe.contactsapp.adapters.ContactAdapter
import com.maxrzhe.contactsapp.data.DummyData
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

        val contacts = DummyData.load()

        binding.rvContacts.apply {
            layoutManager = LinearLayoutManager(this@ContactsListActivity)
            setHasFixedSize(true)
            contactAdapter = ContactAdapter(
                this@ContactsListActivity,
                contacts,
                object : ContactAdapter.OnContactClickListener {
                    override fun onClick(position: Int, contact: Contact) {
                        //go to info activity
                    }
                })
            adapter = contactAdapter
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
                        contactAdapter?.filter?.filter(newText)
                        return false
                    }
                })
        }
        return true
    }

}