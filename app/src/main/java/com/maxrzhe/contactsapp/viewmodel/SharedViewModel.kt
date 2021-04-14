package com.maxrzhe.contactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contactsapp.R
import com.maxrzhe.contactsapp.model.Contact
import com.maxrzhe.contactsapp.repository.Repository
import com.maxrzhe.contactsapp.repository.RepositoryFactory
import com.maxrzhe.contactsapp.repository.RepositoryType
import kotlinx.coroutines.launch

class SharedViewModel(app: Application) : BaseViewModel(app) {

    private val readAllData: LiveData<List<Contact>>
    private val repository: Repository = RepositoryFactory.create(app, RepositoryType.ROOM)
//    private val repository: Repository = RepositoryFactory.create(app, RepositoryType.PLAIN_SQL)

    private var _selectedItem = MutableLiveData<Contact?>(null)
    val selectedItem: LiveData<Contact?> = _selectedItem

    init {
        readAllData = repository.findAll()
    }

    fun select(selectedContact: Contact?) {
        _selectedItem.value = selectedContact
    }

    fun findAll(): LiveData<List<Contact>> {
        return readAllData
    }

    fun add(contact: Contact) {
        viewModelScope.launch {
            repository.add(contact)
        }
    }

    fun update(contact: Contact) {
        viewModelScope.launch {
            repository.update(contact)
        }
    }

    fun detailImageText(
        contact: Contact?,
    ): Int {
        return if (contact == null) {
            R.string.detail_tv_add_image_text
        } else {
            R.string.detail_tv_change_image_text
        }
    }

    fun detailSubmitButtonText(
        contact: Contact?,
    ): Int {
        return if (contact == null) {
            R.string.detail_button_add_text
        } else {
            R.string.detail_button_save_changes_text
        }
    }
}