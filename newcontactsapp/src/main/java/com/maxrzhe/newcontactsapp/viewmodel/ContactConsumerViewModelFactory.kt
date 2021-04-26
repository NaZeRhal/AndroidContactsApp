package com.maxrzhe.newcontactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ContactConsumerViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactConsumerListViewModel::class.java)) {
            return ContactConsumerListViewModel(application) as T
        } else {
            throw IllegalArgumentException("Cannot create instance for class ${modelClass.name}")
        }
    }
}