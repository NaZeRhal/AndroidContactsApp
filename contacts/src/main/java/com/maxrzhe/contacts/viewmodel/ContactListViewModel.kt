package com.maxrzhe.contacts.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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
    val isFavorites = ObservableBoolean()

    init {
        readAllData = repository.findAll()
    }

    fun findAll(): LiveData<List<Contact.Existing>> {
        return if (!isFavorites.get()) readAllData else Transformations.map(readAllData) { items -> items.filter { it.isFavorite == 1 } }
    }

    fun delete(contact: Contact.Existing) {
        viewModelScope.launch {
            repository.delete(contact)
        }
    }

}