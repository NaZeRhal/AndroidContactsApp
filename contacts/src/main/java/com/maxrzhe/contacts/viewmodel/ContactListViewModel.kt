package com.maxrzhe.contacts.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contacts.app.ContactsApp
import com.maxrzhe.contacts.model.ContactMapping
import com.maxrzhe.core.model.Contact
import com.maxrzhe.core.viewmodel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ContactListViewModel(private val app: Application) : BaseViewModel(app) {
    private val compositeDisposable = CompositeDisposable()

    private val readAllData: MutableLiveData<List<Contact.Existing>> = MutableLiveData()

    //    private val repository: Repository = RepositoryFactory.create(app, RepositoryType.PLAIN_SQL)
    var isFavorites: Boolean = false

    init {
//        readAllData = repository.findAll()
        fetchContactsList()
    }

    fun findAll(): LiveData<List<Contact.Existing>> {
        return if (!isFavorites) readAllData else Transformations.map(readAllData) { items -> items.filter { it.isFavorite } }
    }

    fun delete(contact: Contact.Existing) {
        viewModelScope.launch {
//            repository.delete(contact)
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    private fun fetchContactsList() {
        val contactsApi = (app as ContactsApp).contactsApi
        compositeDisposable.add(
            contactsApi.getContactsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        val contacts = ContactMapping.contactRestToContact(response)
                        readAllData.postValue(contacts)
                    }, {
                        readAllData.postValue(null)
                    }
                )
        )
    }
}