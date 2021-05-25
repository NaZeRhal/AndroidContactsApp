package com.example.data_api.repositories

import com.example.data_api.model.Contact
import com.maxrzhe.common.util.Resource
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    fun getContacts(): Flow<Resource<List<Contact>>>

    fun findById(fbId: String): Flow<Resource<Contact>>

    fun add(contact: Contact): Flow<Resource<Contact>>

    fun update(contact: Contact): Flow<Resource<Contact>>

    fun delete(contact: Contact): Flow<Resource<Contact>>
}