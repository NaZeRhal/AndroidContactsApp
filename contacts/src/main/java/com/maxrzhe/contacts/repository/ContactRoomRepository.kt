package com.maxrzhe.contacts.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.maxrzhe.contacts.dao.ContactDao
import com.maxrzhe.contacts.model.ContactMapping
import com.maxrzhe.core.model.Contact

class ContactRoomRepository(private val contactDao: ContactDao) : Repository {

    override suspend fun findById(fbId: String): Contact.Existing? {
        val roomContact = contactDao.findById(fbId)
        return ContactMapping.contactRoomToContact(roomContact)
    }

    override suspend fun add(contact: Contact.New) {
        contactDao.add(ContactMapping.contactToContactRoom(contact))
    }

    override suspend fun addAll(contacts: List<Contact.New>) {
        contactDao.addAll(contacts.map { ContactMapping.contactToContactRoom(it) })
    }

    override suspend fun update(contact: Contact.Existing) {
        contactDao.update(ContactMapping.contactToContactRoom(contact))
    }

    override suspend fun updateAll(contacts: List<Contact.Existing>) {
        contactDao.addAll(contacts.map { ContactMapping.contactToContactRoom(it) })
    }

    override suspend fun delete(contact: Contact.Existing) {
        contactDao.delete(ContactMapping.contactToContactRoom(contact))
    }

    override suspend fun deleteAll(contacts: List<Contact.Existing>) {
        contactDao.deleteAll(contacts.map { ContactMapping.contactToContactRoom(it) })
    }

    override suspend fun findAll(): LiveData<List<Contact.Existing>> {
        val roomContactsLiveData = contactDao.findAll()
        return Transformations.map(roomContactsLiveData) { roomContact ->
            ContactMapping.contactRoomToContact(
                roomContact
            )
        }
    }
}