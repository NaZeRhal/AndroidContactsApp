package com.maxrzhe.contacts.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contacts.repository.Repository
import com.maxrzhe.contacts.repository.RepositoryFactory
import com.maxrzhe.contacts.repository.RepositoryType
import com.maxrzhe.core.model.Contact
import com.maxrzhe.core.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class ContactListViewModel(app: Application) : BaseViewModel(app) {
    private val readAllData: LiveData<List<Contact.Existing>>
    private val repository: Repository = RepositoryFactory.create(app, RepositoryType.PLAIN_SQL)

    init {
        readAllData = repository.findAll()
    }

    fun findAll(): LiveData<List<Contact.Existing>> {
        return readAllData
    }

    fun delete(contact: Contact.Existing) {
        viewModelScope.launch {
            repository.delete(contact)
        }
    }

}