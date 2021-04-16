package com.maxrzhe.contactsapp.repository

import androidx.lifecycle.LiveData
import com.maxrzhe.contactsapp.model.Contact

interface Repository {

    fun findById(id: Long): LiveData<Contact?>

    suspend fun add(contact: Contact)

    suspend fun update(contact: Contact)

    suspend fun delete(contact: Contact)

    fun findAll(): LiveData<List<Contact>>
}