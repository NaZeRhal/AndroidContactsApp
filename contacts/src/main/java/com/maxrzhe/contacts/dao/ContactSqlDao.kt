package com.maxrzhe.contacts.dao

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.contacts.database.DatabaseHandler
import com.maxrzhe.core.model.Contact

class ContactSqlDao private constructor(context: Context) {

    private val dbHandler: DatabaseHandler = DatabaseHandler.getInstance(context)

    private var allContacts = MutableLiveData<List<Contact.Existing>>()

    companion object {
        @Volatile
        private var INSTANCE: ContactSqlDao? = null

        fun getInstance(context: Context): ContactSqlDao {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = ContactSqlDao(context)
                INSTANCE = instance
                return instance
            }
        }
    }

    fun add(contact: Contact.New) {
        var contacts = allContacts.value ?: emptyList()
        val id = dbHandler.add(contact)
        val newContact = Contact.Existing(
            id = id,
            fbId = contact.fbId,
            name = contact.name,
            email = contact.email,
            phone = contact.phone,
            image = contact.image,
            birthDate = contact.birthDate,
            isFavorite = contact.isFavorite
        )
        contacts = contacts + listOf(newContact)
        allContacts.value = contacts
    }

    fun addAll(contacts: List<Contact.New>) {
        var oldContacts = allContacts.value ?: emptyList()
        val ids = dbHandler.addAll(contacts)
        for (index in contacts.indices) {
            val newContact = Contact.Existing(
                id = ids[index],
                fbId = contacts[index].fbId,
                name = contacts[index].name,
                email = contacts[index].email,
                phone = contacts[index].phone,
                image = contacts[index].image,
                birthDate = contacts[index].birthDate,
                isFavorite = contacts[index].isFavorite
            )
            oldContacts = oldContacts + listOf(newContact)
        }
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
        var contacts = allContacts.value ?: emptyList()
        contacts = contacts - listOf(contact)
        dbHandler.delete(contact)
        allContacts.value = contacts
    }

    fun findAll(): LiveData<List<Contact.Existing>> {
        if (allContacts.value == null) {
            allContacts.value = dbHandler.findAll()
        }
        return allContacts
    }

    fun findById(fbId: String): Contact.Existing? {
        return dbHandler.findById(fbId)
    }

    fun updateAll(contacts: List<Contact.Existing>) {
        dbHandler.updateAll(contacts)
    }

    fun deleteAll(contacts: List<Contact.Existing>) {
        dbHandler.deleteAll(contacts)
    }
}