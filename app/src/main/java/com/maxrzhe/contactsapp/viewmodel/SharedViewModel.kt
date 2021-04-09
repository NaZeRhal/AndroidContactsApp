package com.maxrzhe.contactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.contactsapp.database.Repository
import com.maxrzhe.contactsapp.database.SQLRepository
import com.maxrzhe.contactsapp.model.Contact

class SharedViewModel(app: Application) : AndroidViewModel(app) {

    private val repository: Repository = SQLRepository(app.applicationContext)

    private var _selectedContact = MutableLiveData<Contact?>(null)
    val selectedContact: LiveData<Contact?> = _selectedContact

    private val _contacts = MutableLiveData<List<Contact>>()

    fun select(selectedContact: Contact?) {
        _selectedContact.value = selectedContact
    }

    fun getContacts(): LiveData<List<Contact>> {
        if (_contacts.value == null) {
            loadContacts()
        }
        return _contacts
    }

    fun add(contact: Contact) {
        repository.add(contact)
        loadContacts()
    }

    fun update(contact: Contact) {
        repository.update(contact)
        loadContacts()
    }

    private fun loadContacts() {
        val loadedContacts = repository.findAll()
        _contacts.postValue(loadedContacts.value)
    }
}