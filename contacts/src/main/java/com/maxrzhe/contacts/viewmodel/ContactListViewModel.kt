package com.maxrzhe.contacts.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import com.maxrzhe.contacts.remote.Resource
import com.maxrzhe.contacts.repository.ContactRepository
import com.maxrzhe.core.model.Contact
import com.maxrzhe.core.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ContactListViewModel(app: Application) : BaseViewModel(app) {

    private val mainRepo = ContactRepository.getInstance(app)
    var isFavoritesPage: Boolean = false

    private val _errorMessage: MutableLiveData<String?> =
        MutableLiveData(null)
    val errorMessage: LiveData<String?> = _errorMessage

    val isLoading = liveData {
        mainRepo.getContacts().collect {
            when (it) {
                is Resource.Loading -> emit(true)
                is Resource.Error -> {
                    _errorMessage.value = it.error?.message
                    emit(false)
                }
                is Resource.Success -> {
                    _allContacts.value = it.data
                    emit(false)
                }
            }
        }
    }

    private val _allContacts: MutableLiveData<List<Contact>> = MutableLiveData()
    val allContacts: LiveData<List<Contact>>
        get() {
            return if (!isFavoritesPage) _allContacts else Transformations.map(_allContacts) { items ->
                items.filter { it.isFavorite }
            }
        }

    fun delete(contact: Contact) {
        viewModelScope.launch {
            mainRepo.delete(contact)
        }
    }
}