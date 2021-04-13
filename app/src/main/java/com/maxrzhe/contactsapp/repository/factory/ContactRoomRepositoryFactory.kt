package com.maxrzhe.contactsapp.repository.factory

import android.content.Context
import com.maxrzhe.contactsapp.database.ContactDatabase
import com.maxrzhe.contactsapp.repository.ContactRoomRepository
import com.maxrzhe.contactsapp.repository.Repository

class ContactRoomRepositoryFactory : RepositoryFactory {
    override fun create(context: Context): Repository {
        val contactDao = ContactDatabase.getRoomDatabase(context).contactDao()
        return ContactRoomRepository(contactDao)
    }
}