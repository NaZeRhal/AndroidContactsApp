package com.maxrzhe.contactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.maxrzhe.contactsapp.database.Repository

abstract class BaseViewModel<T>(app: Application) : AndroidViewModel(app) {

    protected abstract val repository: Repository<T>

    abstract fun select(selectedContact: T?)

    abstract fun findAll(): LiveData<List<T>>

    abstract fun add(contact: T)

    abstract fun update(contact: T)
}