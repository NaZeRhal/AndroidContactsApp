package com.maxrzhe.contactsfriendapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contactsfriendapp.data.ContactProviderHandler
import com.maxrzhe.contactsfriendapp.model.Contact
import kotlinx.coroutines.launch

class ContactListViewModel(app: Application) : BaseViewModel(app) {

    private var _readAllData = MutableLiveData<List<Contact>>()
    private val readAllData: LiveData<List<Contact>> = _readAllData

    private val providerHandler = ContactProviderHandler(app)

    init {
        viewModelScope.launch {
            _readAllData.value = providerHandler.readAllContacts().value
        }
    }

    fun findAll(): LiveData<List<Contact>> {
        return readAllData
    }

}