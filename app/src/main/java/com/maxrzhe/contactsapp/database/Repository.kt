package com.maxrzhe.contactsapp.database

import androidx.lifecycle.LiveData
import com.maxrzhe.contactsapp.model.Contact

interface Repository<T> {

    fun add(contact: T): Long

    fun update(contact: T)

    fun delete(contact: T)

    fun findAll(): LiveData<List<T>>
}