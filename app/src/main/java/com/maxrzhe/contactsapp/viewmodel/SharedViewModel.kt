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

    private var _selectedItem = MutableLiveData<Contact?>(null)
    val selectedItem: LiveData<Contact?> = _selectedItem

    fun select(selectedContact: Contact?) {
        _selectedItem.value = selectedContact
    }
}