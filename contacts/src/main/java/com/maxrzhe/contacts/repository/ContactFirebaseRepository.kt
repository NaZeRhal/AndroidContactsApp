package com.maxrzhe.contacts.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.contacts.api.ContactsApi
import com.maxrzhe.contacts.data.ContactListResponse
import com.maxrzhe.contacts.model.ContactMapping
import com.maxrzhe.core.model.Contact

class ContactFirebaseRepository(private val contactsApi: ContactsApi) : Repository {
    override suspend fun findById(id: Long): Contact.Existing? {
        val contactItems: ContactListResponse = contactsApi.getContactsListAsync().await()
        val contact = contactItems.first { it.id == id }
        return ContactMapping.contactRestToContact(contact)
    }

    override suspend fun add(contact: Contact.New) {
        TODO("Not yet implemented")
    }

    override suspend fun update(contact: Contact.Existing) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(contact: Contact.Existing) {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(): LiveData<List<Contact.Existing>> {
        val contactItems: ContactListResponse = contactsApi.getContactsListAsync().await()
        val contacts = contactItems.mapNotNull { ContactMapping.contactRestToContact(it) }
        val liveDataContacts: MutableLiveData<List<Contact.Existing>> = MutableLiveData()
        liveDataContacts.value = contacts
        return liveDataContacts
    }

}