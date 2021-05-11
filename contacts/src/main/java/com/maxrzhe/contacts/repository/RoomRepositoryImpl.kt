package com.maxrzhe.contacts.repository

import android.app.Application
import com.maxrzhe.contacts.app.ContactsApp
import com.maxrzhe.contacts.model.ContactMapping
import com.maxrzhe.core.model.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRepositoryImpl private constructor(app: Application) : DatabaseRepository {

    private val contactDao = (app as ContactsApp).contactDao

    override fun findById(fbId: String): Contact {
        val roomContact = contactDao.findById(fbId)
        return ContactMapping.contactRoomToContact(roomContact)
    }

    override suspend fun add(contact: Contact?) {
        contact?.let {
            contactDao.add(ContactMapping.contactToContactRoom(contact))
        }
    }

    override suspend fun addAll(contacts: List<Contact>) {
        contactDao.addAll(contacts.map { ContactMapping.contactToContactRoom(it) })
    }

    override suspend fun update(contact: Contact) {
        contactDao.update(ContactMapping.contactToContactRoom(contact))
    }

    override suspend fun updateAll(contacts: List<Contact>) {
        contactDao.addAll(contacts.map { ContactMapping.contactToContactRoom(it) })
    }

    override fun delete(contact: Contact) {
        contactDao.delete(ContactMapping.contactToContactRoom(contact))
    }

    override suspend fun deleteByQuery(contacts: List<Contact>) {
        contactDao.deleteByFbIds(contacts.map { it.fbId })
    }

    override fun findAll(): Flow<List<Contact>> {
        return contactDao.findAll().map { ContactMapping.contactRoomToContact(it) }
    }

    companion object {
        @Volatile
        private var INSTANCE: RoomRepositoryImpl? = null

        fun getInstance(app: Application): RoomRepositoryImpl {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = RoomRepositoryImpl(app)
                INSTANCE = instance
                return instance
            }
        }
    }
}