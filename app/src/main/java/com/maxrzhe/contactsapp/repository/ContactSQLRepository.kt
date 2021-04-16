package com.maxrzhe.contactsapp.repository

import androidx.lifecycle.LiveData
import com.maxrzhe.contactsapp.dao.ContactSqlDao
import com.maxrzhe.contactsapp.model.Contact

class ContactSQLRepository(private val contactSqlDao: ContactSqlDao) : Repository {


    override fun findById(id: Long): LiveData<Contact?> {
        return contactSqlDao.findById(id)
    }

    override suspend fun add(contact: Contact) {
        contactSqlDao.add(contact)
    }

    override suspend fun update(contact: Contact) {
        contactSqlDao.update(contact)
    }

    override suspend fun delete(contact: Contact) {
        contactSqlDao.delete(contact)
    }

    override fun findAll(): LiveData<List<Contact>> {
        return contactSqlDao.findAll()
    }
}