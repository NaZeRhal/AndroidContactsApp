package com.maxrzhe.contacts.repository

import androidx.lifecycle.LiveData
import com.maxrzhe.contacts.dao.ContactSqlDao
import com.maxrzhe.contacts.repository.Repository
import com.maxrzhe.core.model.Contact

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