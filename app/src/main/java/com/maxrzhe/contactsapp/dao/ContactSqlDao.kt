package com.maxrzhe.contactsapp.dao

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.contactsapp.database.DatabaseHandler
import com.maxrzhe.contactsapp.model.Contact

class ContactSqlDao(context: Context) {

    private val dbHandler: DatabaseHandler = DatabaseHandler(context)

    private var allContacts = MutableLiveData<List<Contact.Existing>>()

    fun add(contact: Contact.New) {
        var contacts = allContacts.value ?: emptyList()
        val id = dbHandler.add(contact)
        val newContact = Contact.Existing(
            id = id,
            name = contact.name,
            email = contact.email,
            phone = contact.phone,
            image = contact.image
        )
        contacts = contacts + listOf(newContact)
        allContacts.value = contacts
    }

    fun update(contact: Contact.Existing) {
        var contacts = allContacts.value ?: emptyList()
        val oldContact = contacts.firstOrNull { it.id == contact.id }
        if (oldContact != null) {
            contacts = contacts - listOf(oldContact)
            contacts = contacts + listOf(contact)
            dbHandler.update(contact)
        }
        allContacts.value = contacts
    }

    fun delete(contact: Contact.Existing) {
        dbHandler.delete(contact)
    }

    fun findAll(): LiveData<List<Contact.Existing>> {
        if (allContacts.value == null) {
            allContacts.value = dbHandler.findAll()
        }
        return allContacts
    }

    fun findById(id: Long): Contact.Existing? {
        return dbHandler.findById(id)
    }
}