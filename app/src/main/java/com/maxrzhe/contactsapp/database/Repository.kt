package com.maxrzhe.contactsapp.database

import androidx.lifecycle.LiveData
import com.maxrzhe.contactsapp.model.Contact

interface Repository {

    fun add(contact: Contact): Long

    fun update(contact: Contact)

    fun delete(contact: Contact)

    fun findAll(): LiveData<List<Contact>>
}