package com.maxrzhe.contacts.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contacts.remote.RemoteDataSourceImpl
import com.maxrzhe.contacts.remote.Result
import com.maxrzhe.contacts.repository.ContactMainRepository
import com.maxrzhe.contacts.repository.RoomRepositoryImpl
import com.maxrzhe.core.model.Contact
import com.maxrzhe.core.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ContactListViewModel(app: Application) : BaseViewModel(app) {

    private val remoteDataSource = RemoteDataSourceImpl(app)
    private val dbRepository = RoomRepositoryImpl.getInstance(app)
    private val mainRepo = ContactMainRepository.getInstance(remoteDataSource, dbRepository)
    val isLoading = ObservableBoolean(true)
    var isFavorites: Boolean = false


    private val _allContacts =
        mainRepo.getContacts().onStart { emit(Result.loading()) }.asLiveData()
    val allContacts: LiveData<Result<List<Contact>>>
        get() {
            return if (!isFavorites) _allContacts else Transformations.map(_allContacts) { items ->
                val data: List<Contact>? = items.data?.filter { it.isFavorite }
                Result.success(data)
            }
        }

    fun delete(contact: Contact) {
        viewModelScope.launch {
            mainRepo.delete(contact)
        }
    }
}