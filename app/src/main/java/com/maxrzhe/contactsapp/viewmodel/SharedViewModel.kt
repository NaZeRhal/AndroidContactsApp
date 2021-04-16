package com.maxrzhe.contactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SharedViewModel(app: Application) : BaseViewModel(app) {

    private var _contactId = MutableLiveData<Long>(0)
    val contactId: LiveData<Long> = _contactId

    fun select(selectedContactId: Long) {
        _contactId.value = selectedContactId
    }
}