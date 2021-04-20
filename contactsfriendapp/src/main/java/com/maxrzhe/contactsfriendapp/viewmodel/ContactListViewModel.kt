package com.maxrzhe.contactsfriendapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.maxrzhe.contactsfriendapp.model.Contact
import com.maxrzhe.contactsfriendapp.repository.Repository
import com.maxrzhe.contactsfriendapp.repository.RepositoryFactory
import com.maxrzhe.contactsfriendapp.repository.RepositoryType

class ContactListViewModel(app: Application) : BaseViewModel(app) {
    private val readAllData: LiveData<List<Contact.Existing>>
//    private val repository: Repository = RepositoryFactory.create(app, RepositoryType.PLAIN_SQL)
    private val repository: Repository = RepositoryFactory.create(app, RepositoryType.ROOM)

    init {
        readAllData = repository.findAll()
    }

    fun findAll(): LiveData<List<Contact.Existing>> {
        return readAllData
    }

}