package com.maxrzhe.contacts.viewmodel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contacts.remote.RemoteDataSourceImpl
import com.maxrzhe.contacts.remote.Result
import com.maxrzhe.contacts.repository.ContactMainRepository
import com.maxrzhe.contacts.repository.RoomRepositoryImpl
import com.maxrzhe.core.model.Contact
import com.maxrzhe.core.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ContactListViewModel(app: Application) : BaseViewModel(app) {

    private val _allContacts: MutableLiveData<Result<List<Contact>>> = MutableLiveData()
    val allContacts: LiveData<Result<List<Contact>>>
        get() {
            return if (!isFavorites) _allContacts else Transformations.map(_allContacts) { items ->
                val data: List<Contact>? = items.data?.filter { it.isFavorite }
                Result.success(data)
            }
        }

    private val remoteDataSource = RemoteDataSourceImpl(app)
    private val dbRepository = RoomRepositoryImpl(app)
    private val mainRepo = ContactMainRepository.getInstance(remoteDataSource, dbRepository)
    val isLoading = ObservableBoolean(true)
    var isFavorites: Boolean = false

    init {
        fetchData()
    }

    fun delete(contact: Contact) {
        viewModelScope.launch {
            mainRepo.delete(contact)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            mainRepo.getContacts().collect {
                _allContacts.value = it
                val cts = allContacts.value
                cts?.let { res ->
                    res.data?.let { list ->
                        for (c in list) {
                            Log.i("DBG", "fetchData: ${c.name}")
                        }
                    }
                }
            }
        }
    }
}