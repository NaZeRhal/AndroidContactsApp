package com.maxrzhe.contactsapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.contactsapp.model.Contact

class SQLRepository(private val context: Context) : Repository {

    private val dbHandler: DatabaseHandler
        get() = DatabaseHandler(context)

    private var _allContacts = MutableLiveData<List<Contact>>()
    private val allContacts: LiveData<List<Contact>> = _allContacts

    override fun add(contact: Contact): Long {
        return dbHandler.add(contact)
    }

    override fun update(contact: Contact) {
        dbHandler.update(contact)
    }

    override fun delete(contact: Contact) {
        dbHandler.delete(contact)
    }

    override fun findAll(): LiveData<List<Contact>> {
        if (_allContacts.value == null) {
            _allContacts.value = dbHandler.findAll()
        }
        return allContacts
    }
}