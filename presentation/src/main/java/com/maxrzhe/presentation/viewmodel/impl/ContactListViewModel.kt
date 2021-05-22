package com.maxrzhe.presentation.viewmodel.impl

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.data_api.model.Contact
import com.maxrzhe.common.util.Resource
import com.maxrzhe.domain.usecases.DeleteContactUseCase
import com.maxrzhe.domain.usecases.GetContactsUseCase
import com.maxrzhe.presentation.model.ContactItemViewModel
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class ContactListViewModel internal constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val deleteContactUseCase: DeleteContactUseCase
) : BaseViewModel() {

    var isFavoritesPage: Boolean = false

    val searchResult = ObservableField<Int>()

    val searchQuery = object : ObservableField<String>() {
        override fun set(value: String?) {
            super.set(value)
            performFiltering(value)
        }
    }

    private val _fbId = MutableLiveData<String>()
    val fbId: LiveData<String> = _fbId

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    val isLoading = liveData {
        val flow: Flow<Resource<List<Contact>>> =
            getContactsUseCase.execute()
        flow.collect {
            when (it) {
                is Resource.Loading -> emit(true)
                is Resource.Error -> {
                    _errorMessage.value = it.error.message
                    emit(false)
                }
                is Resource.Success.Data -> {
                    val items = it.data.map { item ->
                        ContactItemViewModel(
                            contact = item,
                            onItemClick = ::onItemClick
                        )
                    }
                    _allContacts = items
                    emit(false)
                }
            }
        }
    }

    private var _allContacts: List<ContactItemViewModel> = emptyList()
        set(value) {
            field = value
            performFiltering()

        }
    val allContacts: MutableLiveData<List<ContactItemViewModel>> = MutableLiveData(emptyList())

    fun delete(contactItem: ContactItemViewModel) {
        viewModelScope.launch {
            deleteContactUseCase.execute(contactItem.contact).collect {
                if (it is Resource.Success.Completed) {
                    performFiltering()
                } else if (it is Resource.Error) {
                    _errorMessage.value = it.error.message

                }
            }
        }
    }

    private fun onItemClick(item: ContactItemViewModel) {
        _fbId.value = item.contact.fbId
    }

    fun performFiltering() {
        performFiltering(searchQuery.get())
    }

    private fun performFiltering(query: String?) {
        val filteredByFavorites =
            _allContacts.filter { if (isFavoritesPage) it.contact.isFavorite else true }
        val filteredByQuery = if (query == null || query.isEmpty()) {
            filteredByFavorites
        } else {
            val filterPattern: String = query.toLowerCase(Locale.getDefault()).trim()
            filteredByFavorites
                .filter { anyMatches(it.contact, filterPattern) }
        }
        if (filteredByFavorites.size != filteredByQuery.size) {
            searchResult.set(filteredByQuery.size)
        } else {
            searchResult.set(-1)
        }
        allContacts.value = filteredByQuery
    }

    private fun anyMatches(contact: Contact, pattern: String): Boolean {
        return with(contact) {
            name.toLowerCase(Locale.getDefault()).contains(pattern) ||
                    email.toLowerCase(Locale.getDefault()).contains(pattern) ||
                    phone.toLowerCase(Locale.getDefault()).contains(pattern)
        }
    }
}