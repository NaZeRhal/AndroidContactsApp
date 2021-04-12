package com.maxrzhe.contactsapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.contactsapp.database.ContactDatabase
import com.maxrzhe.contactsapp.database.ContactRepository
import com.maxrzhe.contactsapp.model.Contact

class SharedViewModel(app: Application) : BaseViewModel(app) {

    private val readAllData: LiveData<List<Contact>>
    private val contactRepository: ContactRepository

    private var _selectedItem = MutableLiveData<Contact?>(null)
    val selectedItem: LiveData<Contact?> = _selectedItem

    init {
        val contactRoomDao = ContactDatabase.getRoomDatabase(app).contactDao()
        contactRepository = ContactRepository(contactRoomDao)
        readAllData = contactRepository.findAll()
    }

    override fun select(selectedContact: Contact?) {
        _selectedItem.value = selectedContact
    }

    override fun findAll(): LiveData<List<Contact>> {
        Log.i("ALL_CONTACTS", "setup: null = ${readAllData.value.isNullOrEmpty()}")
        readAllData.value?.forEach {
            Log.i("ALL_CONTACTS", "setup: name = ${it.name}")
        }
        return readAllData
    }

    override suspend fun add(contact: Contact) {
        contactRepository.add(contact)
    }

    override suspend fun update(contact: Contact) {
        contactRepository.update(contact)
    }
}