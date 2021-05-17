package com.maxrzhe.presentation.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maxrzhe.presentation.viewmodel.impl.ContactDetailViewModel
import com.maxrzhe.presentation.viewmodel.impl.ContactListViewModel

class BaseViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ContactListViewModel::class.java) -> {
                ContactListViewModel(application) as T
            }
            modelClass.isAssignableFrom(ContactDetailViewModel::class.java) -> {
                ContactDetailViewModel(application) as T
            }
            else -> {
                throw IllegalArgumentException("Cannot create instance for class ${modelClass.name}")
            }
        }
    }
}