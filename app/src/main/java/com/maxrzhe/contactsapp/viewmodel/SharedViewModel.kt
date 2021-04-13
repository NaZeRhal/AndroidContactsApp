package com.maxrzhe.contactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contactsapp.model.Contact
import com.maxrzhe.contactsapp.repository.Repository
import com.maxrzhe.contactsapp.repository.factory.ContactRoomRepositoryFactory
//import com.maxrzhe.contactsapp.repository.factory.ContactSqlRepositoryFactory
import kotlinx.coroutines.launch

class SharedViewModel(app: Application) : BaseViewModel(app) {

    private val readAllData: LiveData<List<Contact>>
    private val repository: Repository = ContactRoomRepositoryFactory().create(app)
//    private val repository: Repository = ContactSqlRepositoryFactory().create(app)

    private var _selectedItem = MutableLiveData<Contact?>(null)
    val selectedItem: LiveData<Contact?> = _selectedItem

    init {
        readAllData = repository.findAll()
    }

    override fun select(selectedContact: Contact?) {
        _selectedItem.value = selectedContact
    }

    override fun findAll(): LiveData<List<Contact>> {
        return readAllData
    }

    override fun add(contact: Contact) {
        viewModelScope.launch {
            repository.add(contact)
        }
    }

    override fun update(contact: Contact) {
        viewModelScope.launch {
            repository.update(contact)
        }
    }
}