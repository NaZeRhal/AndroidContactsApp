package com.maxrzhe.contacts.repository

import com.maxrzhe.core.model.Contact

interface DatabaseRepository {

    fun findById(fbId: String): Contact

    fun add(contact: Contact?)

    fun addAll(contacts: List<Contact>)

    fun update(contact: Contact)

    fun updateAll(contacts: List<Contact>)

    fun delete(contact: Contact)

    fun deleteByQuery(contacts: List<Contact>)

    fun findAll(): List<Contact>?
}