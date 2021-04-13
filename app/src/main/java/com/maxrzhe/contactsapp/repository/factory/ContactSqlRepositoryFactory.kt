package com.maxrzhe.contactsapp.repository.factory

import android.content.Context
import com.maxrzhe.contactsapp.dao.ContactSqlDao
import com.maxrzhe.contactsapp.repository.Repository
import com.maxrzhe.contactsapp.repository.ContactSQLRepository

class ContactSqlRepositoryFactory : RepositoryFactory {
    override fun create(context: Context): Repository {
        val contactSqlDao = ContactSqlDao(context)
        return ContactSQLRepository(contactSqlDao)
    }
}