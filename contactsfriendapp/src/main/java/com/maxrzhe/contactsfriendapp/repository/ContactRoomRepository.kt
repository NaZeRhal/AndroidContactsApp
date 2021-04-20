package com.maxrzhe.contactsfriendapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.maxrzhe.contactsfriendapp.dao.ContactDao
import com.maxrzhe.contactsfriendapp.model.Contact
import com.maxrzhe.contactsfriendapp.model.ContactMapping

class ContactRoomRepository(private val contactDao: ContactDao) : Repository {

    override suspend fun findById(id: Long): Contact.Existing? {
        val roomContact = contactDao.findById(id)
        return ContactMapping.contactRoomToContact(roomContact)
    }

    override suspend fun add(contact: Contact.New) {
        contactDao.add(ContactMapping.contactToContactRoom(contact))
    }

    override suspend fun update(contact: Contact.Existing) {
        contactDao.update(ContactMapping.contactToContactRoom(contact))
    }

    override suspend fun delete(contact: Contact.Existing) {
        contactDao.delete(ContactMapping.contactToContactRoom(contact))
    }

    override fun findAll(): LiveData<List<Contact.Existing>> {
        val roomContactsLiveData = contactDao.findAll()
        return Transformations.map(roomContactsLiveData) { roomContact ->
            ContactMapping.contactRoomToContact(
                roomContact
            )
        }
    }
}