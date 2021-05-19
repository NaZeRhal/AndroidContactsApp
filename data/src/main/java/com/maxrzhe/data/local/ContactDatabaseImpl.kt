package com.maxrzhe.data.local

import com.maxrzhe.data.entities.room.ContactRoom
import com.maxrzhe.data.local.room.ContactDao
import kotlinx.coroutines.flow.Flow

class ContactDatabaseImpl internal constructor(private val contactDao: ContactDao) :
    ContactDatabase {
    override suspend fun add(contactRoom: ContactRoom) {
        contactDao.add(contactRoom)
    }

    override suspend fun addAll(contacts: List<ContactRoom>) {
        contactDao.addAll(contacts)
    }

    override fun delete(contactRoom: ContactRoom) {
        contactDao.delete(contactRoom)
    }

    override fun deleteAll() {
        contactDao.deleteAll()
    }

    override suspend fun deleteByFbIds(fbIds: List<String>) {
        contactDao.deleteByFbIds(fbIds)
    }

    override fun findById(fbId: String): Flow<ContactRoom> {
        return contactDao.findById(fbId)
    }

    override fun findAll(): Flow<List<ContactRoom>> {
        return contactDao.findAll()
    }

    override suspend fun refreshAll(contacts: List<ContactRoom>) {
        contactDao.refreshAll(contacts)
    }

    override suspend fun refreshByIds(ids: List<String>, contacts: List<ContactRoom>) {
        contactDao.refreshByIds(ids, contacts)
    }
}