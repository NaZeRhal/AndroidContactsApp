package com.maxrzhe.contacts.repository

import androidx.lifecycle.LiveData
import com.maxrzhe.contacts.model.ContactMapping
import com.maxrzhe.contacts.api.ContactsApi
import com.maxrzhe.core.model.Contact
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ContactFirebaseRepository(private val contactsApi: ContactsApi): Repository {
    override suspend fun findById(id: Long): Contact.Existing? {
        TODO("Not yet implemented")
    }

    override suspend fun add(contact: Contact.New) {
        TODO("Not yet implemented")
    }

    override suspend fun update(contact: Contact.Existing) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(contact: Contact.Existing) {
        TODO("Not yet implemented")
    }

    override fun findAll(): LiveData<List<Contact.Existing>> {
        TODO("Not yet implemented")
//        compositeDisposable.add(
//            contactsApi.getContactsList()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    { response ->
//                        val contactItems = response.contacts
//                        val contacts = ContactMapping.contactRestToContact(contactItems)
//                        readAllData.postValue(contacts)
//                    }, {
//                        readAllData.postValue(null)
//                    }
//                )
//        )
    }

}