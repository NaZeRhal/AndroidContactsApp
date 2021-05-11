package com.maxrzhe.contacts.repository

import com.maxrzhe.core.model.Contact
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    fun findById(fbId: String): Contact

    suspend  fun add(contact: Contact?)

    suspend fun addAll(contacts: List<Contact>)

    suspend fun update(contact: Contact)

    suspend fun updateAll(contacts: List<Contact>)

    fun delete(contact: Contact)

    suspend fun deleteByQuery(contacts: List<Contact>)

    fun findAll(): Flow<List<Contact>>
}