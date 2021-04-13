package com.maxrzhe.contactsapp.repository

import androidx.lifecycle.LiveData
import com.maxrzhe.contactsapp.dao.ContactDao
import com.maxrzhe.contactsapp.model.Contact

class ContactRoomRepository(private val contactDao: ContactDao) : Repository{
    override suspend fun add(contact: Contact) {
        contactDao.add(contact)
    }

    override suspend fun update(contact: Contact) {
        contactDao.update(contact)
    }

    override suspend fun delete(contact: Contact) {
        contactDao.delete(contact)
    }

    override fun findAll(): LiveData<List<Contact>> {
        return contactDao.findAll()
    }
}