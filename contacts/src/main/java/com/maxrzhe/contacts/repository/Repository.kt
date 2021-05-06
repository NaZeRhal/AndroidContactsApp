package com.maxrzhe.contacts.repository

import androidx.lifecycle.LiveData
import com.maxrzhe.core.model.Contact

interface Repository {

    suspend fun findById(fbId: String): Contact.Existing?

    suspend fun add(contact: Contact.New)

    suspend fun addAll(contacts: List<Contact.New>)

    suspend fun update(contact: Contact.Existing)

    suspend fun updateAll(contacts: List<Contact.Existing>)

    suspend fun delete(contact: Contact.Existing)

    suspend fun deleteAll(contacts: List<Contact.Existing>)

    suspend fun findAll(): LiveData<List<Contact.Existing>>
}