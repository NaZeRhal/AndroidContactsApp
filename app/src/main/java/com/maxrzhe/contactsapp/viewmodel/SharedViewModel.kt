package com.maxrzhe.contactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.contactsapp.database.Repository
import com.maxrzhe.contactsapp.database.SQLRepository
import com.maxrzhe.contactsapp.model.Contact

class SharedViewModel(private val app: Application) : AndroidViewModel(app) {

    private val repository: Repository
        get() = SQLRepository(app.applicationContext)

    private var _contact = MutableLiveData<Contact?>(null)
    val contact: LiveData<Contact?> = _contact

    private val _contacts = MutableLiveData<List<Contact>>()

    fun select(selectedContact: Contact?) {
        _contact.value = selectedContact
    }

    fun getContacts(): LiveData<List<Contact>> {
        if (_contacts.value == null) {
            loadContacts()
        }
        return _contacts
    }

    fun addOrUpdate(contact: Contact) {
        var contacts = _contacts.value ?: emptyList()
        val oldContact = contacts.firstOrNull { it.id == contact.id }

        if (oldContact != null) {
            contacts = contacts - listOf(oldContact)
            contacts = contacts + listOf(contact)
            repository.update(contact)
        } else {
            val id = repository.add(contact)
            val newContact = Contact(
                id = id,
                name = contact.name,
                email = contact.email,
                phone = contact.phone,
                image = contact.image
            )
            contacts = contacts + listOf(newContact)
        }
        _contacts.value = contacts
    }

    private fun loadContacts() {
        val loadedContacts = repository.findAll()
        _contacts.postValue(loadedContacts.value)
    }
}