package com.maxrzhe.contacts.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contacts.remote.Resource
import com.maxrzhe.contacts.repository.ContactRepository
import com.maxrzhe.core.model.Contact
import com.maxrzhe.core.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ContactListViewModel(app: Application) : BaseViewModel(app) {

    private val mainRepo = ContactRepository.getInstance(app)
    var isFavoritesPage: Boolean = false
        set(value) {
            field = value
        }

    private val _errorMessage: MutableLiveData<String> =
        MutableLiveData()
    val errorMessage: LiveData<String> = _errorMessage

    val isLoading = liveData {
        mainRepo.getContacts().collect {
            when (it) {
                is Resource.Loading -> emit(true)
                is Resource.Error -> {
                    _errorMessage.value = it.error?.message
                    emit(false)
                }
                is Resource.Success.Data -> {
                    _allContacts = it.data
                    emit(false)
                }
            }
        }
    }

    private var _allContacts: List<Contact> = emptyList()
        set(value) {
            field = value
            allContacts.value = value.filter { if (isFavoritesPage) it.isFavorite else true }
        }
    val allContacts: MutableLiveData<List<Contact>> = MutableLiveData(emptyList())

    fun delete(contact: Contact) {
        viewModelScope.launch {
            mainRepo.delete(contact)
        }
    }
}