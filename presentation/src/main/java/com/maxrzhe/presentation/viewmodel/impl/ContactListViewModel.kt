package com.maxrzhe.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.maxrzhe.common.util.Resource
import com.maxrzhe.domain.model.Contact
import com.maxrzhe.domain.usecases.DeleteContactUseCase
import com.maxrzhe.domain.usecases.GetContactsUseCase
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ContactListViewModel internal constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val deleteContactUseCase: DeleteContactUseCase
) : BaseViewModel() {

    var isFavoritesPage: Boolean = false

    private val _errorMessage: MutableLiveData<String> =
        MutableLiveData()
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
            deleteContactUseCase.execute(contact)
        }
    }
}