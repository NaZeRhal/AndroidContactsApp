package com.maxrzhe.contacts.repository

import androidx.lifecycle.LiveData
import com.maxrzhe.contacts.dao.ContactSqlDao
import com.maxrzhe.core.model.Contact

class ContactSQLRepository(private val contactSqlDao: ContactSqlDao) : Repository {

    override suspend fun findById(fbId: String): Contact.Existing? {
        return contactSqlDao.findById(fbId)
    }

    override suspend fun add(contact: Contact.New) {
        contactSqlDao.add(contact)
    }

    override suspend fun addAll(contacts: List<Contact.New>) {
        contactSqlDao.addAll(contacts)
    }

    override suspend fun update(contact: Contact.Existing) {
        contactSqlDao.update(contact)
    }

    override suspend fun updateAll(contacts: List<Contact.Existing>) {
        contactSqlDao.updateAll(contacts)
    }

    override suspend fun delete(contact: Contact.Existing) {
        contactSqlDao.delete(contact)
    }

    override suspend fun deleteAll(contacts: List<Contact.Existing>) {
        contactSqlDao.deleteAll(contacts)
    }

    override suspend fun findAll(): LiveData<List<Contact.Existing>> {
        return contactSqlDao.findAll()
    }
}