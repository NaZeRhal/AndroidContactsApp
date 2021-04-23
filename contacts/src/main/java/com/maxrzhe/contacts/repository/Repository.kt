package com.maxrzhe.contacts.repository

import androidx.lifecycle.LiveData
import com.maxrzhe.core.model.Contact

interface Repository {

    suspend fun findById(id: Long): Contact.Existing?

    suspend fun add(contact: Contact.New)

    suspend fun update(contact: Contact.Existing)

    suspend fun delete(contact: Contact.Existing)

    fun findAll(): LiveData<List<Contact.Existing>>
}