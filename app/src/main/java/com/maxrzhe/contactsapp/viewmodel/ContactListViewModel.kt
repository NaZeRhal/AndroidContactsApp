package com.maxrzhe.contactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.maxrzhe.contactsapp.model.Contact
import com.maxrzhe.contactsapp.repository.Repository
import com.maxrzhe.contactsapp.repository.RepositoryFactory
import com.maxrzhe.contactsapp.repository.RepositoryType

class ContactListViewModel(app: Application) : BaseViewModel(app) {
    private val readAllData: LiveData<List<Contact>>
    private val repository: Repository = RepositoryFactory.create(app, RepositoryType.ROOM)

    init {
        readAllData = repository.findAll()
    }

    fun findAll(): LiveData<List<Contact>> {
        return readAllData
    }

}