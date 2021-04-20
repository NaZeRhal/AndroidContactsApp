package com.maxrzhe.contactsfriendapp.repository

import android.content.Context
import com.maxrzhe.contactsfriendapp.dao.ContactSqlDao
import com.maxrzhe.contactsfriendapp.database.ContactRoomDatabase

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