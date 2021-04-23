package com.maxrzhe.contacts.repository

import android.content.Context
import com.maxrzhe.contacts.dao.ContactSqlDao
import com.maxrzhe.contacts.database.ContactRoomDatabase
import com.maxrzhe.contacts.repository.ContactRoomRepository
import com.maxrzhe.contacts.repository.ContactSQLRepository
import com.maxrzhe.contacts.repository.Repository

abstract class RepositoryFactory {

    companion object {
        fun create(context: Context, type: RepositoryType): Repository {
            return when (type) {
                RepositoryType.ROOM -> {
                    val contactDao = ContactRoomDatabase.getInstance(context).contactDao()
                    ContactRoomRepository(contactDao)
                }
                RepositoryType.PLAIN_SQL -> {
                    val contactSqlDao = ContactSqlDao.getInstance(context)
                    ContactSQLRepository(contactSqlDao)
                }
            }
        }
    }
}