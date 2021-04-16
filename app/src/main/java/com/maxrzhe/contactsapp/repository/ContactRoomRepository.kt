package com.maxrzhe.contactsapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.maxrzhe.contactsapp.dao.ContactDao
import com.maxrzhe.contactsapp.model.Contact
import com.maxrzhe.contactsapp.model.ContactMapping

class ContactRoomRepository(private val contactDao: ContactDao) : Repository {

    override suspend fun findById(id: Long): Contact {
        val roomContact = contactDao.findById(id)
        return ContactMapping.contactRoomToContact(roomContact)
    }

    override suspend fun add(contact: Contact) {
        contactDao.add(ContactMapping.contactToContactRoom(contact))
    }

    override suspend fun update(contact: Contact) {
        contactDao.update(ContactMapping.contactToContactRoom(contact))
    }

    override suspend fun delete(contact: Contact) {
        contactDao.delete(ContactMapping.contactToContactRoom(contact))
    }

    override fun findAll(): LiveData<List<Contact>> {
        val roomContactsLiveData = contactDao.findAll()
        return Transformations.map(roomContactsLiveData) { roomContact ->
            ContactMapping.contactRoomToContact(
                roomContact
            )
        }
    }
}