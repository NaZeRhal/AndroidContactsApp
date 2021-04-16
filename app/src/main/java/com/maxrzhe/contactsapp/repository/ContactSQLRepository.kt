package com.maxrzhe.contactsapp.repository

import androidx.lifecycle.LiveData
import com.maxrzhe.contactsapp.dao.ContactSqlDao
import com.maxrzhe.contactsapp.model.Contact

class ContactSQLRepository(private val contactSqlDao: ContactSqlDao) : Repository {


    override suspend fun findById(id: Long): Contact.Existing? {
        return contactSqlDao.findById(id)
    }

    override suspend fun add(contact: Contact.New) {
        contactSqlDao.add(contact)
    }

    override suspend fun update(contact: Contact.Existing) {
        contactSqlDao.update(contact)
    }

    override suspend fun delete(contact: Contact.Existing) {
        contactSqlDao.delete(contact)
    }

    override fun findAll(): LiveData<List<Contact.Existing>> {
        return contactSqlDao.findAll()
    }
}