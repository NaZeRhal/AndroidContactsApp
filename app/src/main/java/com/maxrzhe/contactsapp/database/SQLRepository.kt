package com.maxrzhe.contactsapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.contactsapp.model.Contact

class SQLRepository(context: Context) : Repository {

    private val dbHandler: DatabaseHandler = DatabaseHandler(context)

    private var allContacts = MutableLiveData<List<Contact>>()

    override fun add(contact: Contact): Long {
        var contacts = allContacts.value ?: emptyList()
        val id = dbHandler.add(contact)
        val newContact = Contact(
            id = id,
            name = contact.name,
            email = contact.email,
            phone = contact.phone,
            image = contact.image
        )
        contacts = contacts + listOf(newContact)
        allContacts.value = contacts
        return id
    }

    override fun update(contact: Contact) {
        var contacts = allContacts.value ?: emptyList()
        val oldContact = contacts.firstOrNull { it.id == contact.id }
        if (oldContact != null) {
            contacts = contacts - listOf(oldContact)
            contacts = contacts + listOf(contact)
            dbHandler.update(contact)
        }
        allContacts.value = contacts
    }

    override fun delete(contact: Contact) {
        dbHandler.delete(contact)
    }

    override fun findAll(): LiveData<List<Contact>> {
        if (allContacts.value == null) {
            allContacts.value = dbHandler.findAll()
        }
        return allContacts
    }
}