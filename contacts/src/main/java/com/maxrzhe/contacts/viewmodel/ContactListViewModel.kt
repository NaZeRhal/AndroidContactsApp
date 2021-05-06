package com.maxrzhe.contacts.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contacts.repository.ContactRepoWithRestApi
import com.maxrzhe.contacts.repository.Repository
import com.maxrzhe.contacts.repository.RepositoryType
import com.maxrzhe.core.model.Contact
import com.maxrzhe.core.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class ContactListViewModel(app: Application) : BaseViewModel(app) {

    private val readAllData: MutableLiveData<List<Contact.Existing>> = MutableLiveData()

    private val repository: Repository =
        ContactRepoWithRestApi.getInstance(app, RepositoryType.PLAIN_SQL)
    var isFavorites: Boolean = false

    init {
        loadData()
    }

    fun findAll(): LiveData<List<Contact.Existing>> {
        return if (!isFavorites) readAllData else Transformations.map(readAllData) { items -> items.filter { it.isFavorite } }
    }

    fun delete(contact: Contact.Existing) {
        viewModelScope.launch {
            repository.delete(contact)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val findAll = repository.findAll()
            readAllData.value = findAll.value
        }
    }
}