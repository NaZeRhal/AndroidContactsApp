package com.maxrzhe.contacts.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.contacts.app.ContactsApp
import com.maxrzhe.contacts.data.ContactListResponse
import com.maxrzhe.contacts.model.ContactMapping
import com.maxrzhe.core.model.Contact

class ContactRepoWithRestApi private constructor(app: Application, type: RepositoryType) :
    Repository {

    private val contactsApi = (app as ContactsApp).contactsApi
    private val repository = RepositoryFactory.create(app, type)
    private val allContactsList: MutableLiveData<List<Contact.Existing>> = MutableLiveData()

    override suspend fun findById(fbId: String): Contact.Existing? {
        return null
    }

    override suspend fun add(contact: Contact.New) {
        val response = contactsApi.postContactAsync(contact).await()
        if (response.isNotEmpty()) {
            repository.add(contact)
        }
    }

    override suspend fun addAll(contacts: List<Contact.New>) {
        repository.addAll(contacts)
    }

    override suspend fun update(contact: Contact.Existing) {
        repository.update(contact)
    }

    override suspend fun updateAll(contacts: List<Contact.Existing>) {
        repository.updateAll(contacts)
    }

    override suspend fun delete(contact: Contact.Existing) {
        repository.delete(contact)
    }

    override suspend fun deleteAll(contacts: List<Contact.Existing>) {
        repository.deleteAll(contacts)
    }


    override suspend fun findAll(): LiveData<List<Contact.Existing>> {
        val dbContacts: LiveData<List<Contact.Existing>> = repository.findAll()
        allContactsList.value = dbContacts.value
        val apiContacts: ContactListResponse = contactsApi.getContactsListAsync().await()

        val contacts = dbContacts.value
        val existing = apiContacts.mapNotNull {
            val oldContact = exist(it.key, contacts)
            if (oldContact != null) {
                ContactMapping.contactRestToContactExisting(it.key, it.value, oldContact)
            } else null
        }

        repository.updateAll(existing)

        val notExisting = apiContacts
            .filter { exist(it.key, contacts) == null }
            .map {
                ContactMapping.contactRestToContactNew(it.key, it.value)
            }

        repository.addAll(notExisting)

        val contactsForDeleting = contacts?.filterNot { apiContacts.containsKey(it.fbId) }
        contactsForDeleting?.let {
            deleteAll(contactsForDeleting)
        }

        val updatedContacts: LiveData<List<Contact.Existing>> = repository.findAll()
        allContactsList.value = updatedContacts.value
        return allContactsList
    }


    private fun exist(
        fbId: String,
        contacts: List<Contact.Existing>?
    ): Contact.Existing? {
        return contacts?.firstOrNull { it.fbId == fbId }
    }


    companion object {
        @Volatile
        private var INSTANCE: ContactRepoWithRestApi? = null

        fun getInstance(app: Application, type: RepositoryType): ContactRepoWithRestApi {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = ContactRepoWithRestApi(app, type)
                INSTANCE = instance
                return instance
            }
        }
    }
}