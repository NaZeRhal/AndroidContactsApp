package com.maxrzhe.contactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.maxrzhe.contactsapp.model.Contact

abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

    abstract fun select(selectedContact: Contact?)

    abstract fun findAll(): LiveData<List<Contact>>

    abstract fun add(contact: Contact)

    abstract fun update(contact: Contact)
}