package com.maxrzhe.contactsapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.maxrzhe.contactsapp.dao.ContactDao
import com.maxrzhe.contactsapp.model.Contact
import com.maxrzhe.contactsapp.model.ContactMapping

class ContactRoomRepository(private val contactDao: ContactDao) : Repository {

    override fun findById(id: Long): LiveData<Contact?> {
        val roomContact = contactDao.findById(id)
        return Transformations.map(roomContact) { contact ->
            ContactMapping.contactRoomToContact(
                contact
            )
        }
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