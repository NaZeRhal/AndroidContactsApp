package com.maxrzhe.domain.repositories

import com.maxrzhe.common.util.Resource
import com.maxrzhe.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    fun getContacts(): Flow<Resource<List<Contact>>>

    fun findById(fbId: String): Flow<Resource<Contact>>

    fun add(contact: Contact): Flow<Resource<Contact>>

    fun update(contact: Contact): Flow<Resource<Contact>>

    suspend fun delete(contact: Contact)
}