package com.maxrzhe.newcontactsapp.viewmodel

import androidx.lifecycle.LiveData
import com.example.data_api.model.Contact
import com.maxrzhe.data.provider.ContactProviderHandler
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

class ContactConsumerListViewModel(providerHandler: ContactProviderHandler) : BaseViewModel() {

    private var readAllData: LiveData<List<Contact>> = providerHandler.findAll()

    fun findAll(): LiveData<List<Contact>> {
        return readAllData
    }

}