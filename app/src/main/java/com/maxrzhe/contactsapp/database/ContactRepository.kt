package com.maxrzhe.contactsapp.database

import androidx.lifecycle.LiveData
import com.maxrzhe.contactsapp.model.Contact

class ContactRepository(private val contactDao: ContactDao){
    suspend fun add(contact: Contact) {
        contactDao.add(contact)
    }

    suspend fun update(contact: Contact) {
        contactDao.update(contact)
    }

    suspend fun delete(contact: Contact) {
        contactDao.delete(contact)
    }

    fun findAll(): LiveData<List<Contact>> {
        return contactDao.findAll()
    }
}