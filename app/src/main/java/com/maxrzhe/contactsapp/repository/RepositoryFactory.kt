package com.maxrzhe.contactsapp.repository

import android.content.Context
import com.maxrzhe.contactsapp.dao.ContactSqlDao
import com.maxrzhe.contactsapp.database.ContactDatabase

abstract class RepositoryFactory {

    companion object {
        fun create(context: Context, type: RepositoryType): Repository {
            return when (type) {
                RepositoryType.ROOM -> {
                    val contactDao = ContactDatabase.getRoomDatabase(context).contactDao()
                    ContactRoomRepository(contactDao)
                }
                RepositoryType.PLAIN_SQL -> {
                    val contactSqlDao = ContactSqlDao(context)
                    ContactSQLRepository(contactSqlDao)
                }
            }
        }
    }
}