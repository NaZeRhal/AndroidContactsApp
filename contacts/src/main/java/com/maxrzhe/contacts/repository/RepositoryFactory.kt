package com.maxrzhe.contacts.repository

import android.app.Application
import com.maxrzhe.contacts.app.ContactsApp
import com.maxrzhe.contacts.dao.ContactSqlDao
import com.maxrzhe.contacts.database.ContactRoomDatabase

abstract class RepositoryFactory {

    companion object {
        fun create(application: Application, type: RepositoryType): Repository {
            return when (type) {
                RepositoryType.ROOM -> {
                    val contactDao = ContactRoomDatabase.getInstance(application).contactDao()
                    ContactRoomRepository(contactDao)
                }
                RepositoryType.PLAIN_SQL -> {
                    val contactSqlDao = ContactSqlDao.getInstance(application)
                    ContactSQLRepository(contactSqlDao)
                }
                RepositoryType.FIREBASE_REST_API -> {
                    val contactsApi = (application as ContactsApp).contactsApi
                    ContactFirebaseRepository(contactsApi)
                }
            }
        }
    }
}