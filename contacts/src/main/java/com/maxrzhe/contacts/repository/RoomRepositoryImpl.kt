package com.maxrzhe.contacts.repository

import android.app.Application
import android.util.Log
import com.maxrzhe.contacts.app.ContactsApp
import com.maxrzhe.contacts.model.ContactMapping
import com.maxrzhe.core.model.Contact

class RoomRepositoryImpl(app: Application) : DatabaseRepository {

    private val contactDao = (app as ContactsApp).contactDao

    override fun findById(fbId: String): Contact {
        val roomContact = contactDao.findById(fbId)
        return ContactMapping.contactRoomToContact(roomContact)
    }

    override fun add(contact: Contact?) {
        contact?.let {
            contactDao.add(ContactMapping.contactToContactRoom(contact))
        }
    }

    override fun addAll(contacts: List<Contact>) {
        contactDao.addAll(contacts.map { ContactMapping.contactToContactRoom(it) })
    }

    override fun update(contact: Contact) {
        contactDao.update(ContactMapping.contactToContactRoom(contact))
    }

    override fun updateAll(contacts: List<Contact>) {
        contactDao.addAll(contacts.map { ContactMapping.contactToContactRoom(it) })
    }

    override fun delete(contact: Contact) {
        contactDao.delete(ContactMapping.contactToContactRoom(contact))
    }

    override fun deleteByQuery(contacts: List<Contact>) {
        contactDao.deleteByFbIds(contacts.map { it.fbId })
    }

    override fun findAll(): List<Contact> {
        val roomContacts = contactDao.findAll()
        for (c in roomContacts) {
            Log.i("DBG", "findAll: ${c.name}")
        }
        return roomContacts.map { ContactMapping.contactRoomToContact(it) }
    }
}