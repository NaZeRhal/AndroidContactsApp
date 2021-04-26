package com.maxrzhe.newcontactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.maxrzhe.contacts.provider.ContactProviderHandler
import com.maxrzhe.contacts.repository.Repository
import com.maxrzhe.core.model.Contact
import com.maxrzhe.core.viewmodel.BaseViewModel

class ContactConsumerListViewModel(app: Application) : BaseViewModel(app) {
    private val readAllData: LiveData<List<Contact.Existing>>
    private val repository: Repository = ContactProviderHandler(app)

    init {
        readAllData = repository.findAll()
    }

    fun findAll(): LiveData<List<Contact.Existing>> {
        return readAllData
    }

}