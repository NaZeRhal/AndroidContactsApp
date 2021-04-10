package com.maxrzhe.contactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.contactsapp.database.Repository
import com.maxrzhe.contactsapp.database.SQLRepository
import com.maxrzhe.contactsapp.model.Contact

class SharedViewModel(app: Application) : BaseViewModel<Contact>(app) {

    override val repository: Repository<Contact> = SQLRepository(app.applicationContext)

    private var _selectedItem = MutableLiveData<Contact?>(null)
    val selectedItem: LiveData<Contact?> = _selectedItem

    private val _allItems = MutableLiveData<List<Contact>>()

    override fun select(selectedContact: Contact?) {
        _selectedItem.value = selectedContact
    }

    override fun findAll(): LiveData<List<Contact>> {
        if (_allItems.value == null) {
            loadContacts()
        }
        return _allItems
    }

    override fun add(contact: Contact) {
        repository.add(contact)
        loadContacts()
    }

    override fun update(contact: Contact) {
        repository.update(contact)
        loadContacts()
    }

    private fun loadContacts() {
        val loadedContacts = repository.findAll()
        _allItems.postValue(loadedContacts.value)
    }
}