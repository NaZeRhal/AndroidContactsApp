package com.maxrzhe.newcontactsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maxrzhe.data.provider.ContactProviderHandler

class ContactConsumerViewModelFactory(private val providerHandler: ContactProviderHandler) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactConsumerListViewModel::class.java)) {
            return ContactConsumerListViewModel(providerHandler) as T
        } else {
            throw IllegalArgumentException("Cannot create instance for class ${modelClass.name}")
        }
    }
}