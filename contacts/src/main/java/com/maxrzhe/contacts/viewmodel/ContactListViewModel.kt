package com.maxrzhe.contacts.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contacts.remote.Resource
import com.maxrzhe.contacts.repository.ContactRepository
import com.maxrzhe.core.model.Contact
import com.maxrzhe.core.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class ContactListViewModel(app: Application) : BaseViewModel(app) {

    private val mainRepo = ContactRepository.getInstance(app)
    val isLoading = ObservableBoolean(true)
    var isFavorites: Boolean = false

    private val _allContacts =
        mainRepo.getContacts().asLiveData()
    val allContacts: LiveData<Resource<List<Contact>>>
        get() {
            return if (!isFavorites) _allContacts else Transformations.map(_allContacts) { items ->
                val data: List<Contact>? = items.data?.filter { it.isFavorite }
                Resource.Success(data)
            }
        }

    fun delete(contact: Contact) {
        viewModelScope.launch {
            mainRepo.delete(contact)
        }
    }
}