package com.maxrzhe.contacts.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.core.viewmodel.BaseViewModel

class SharedViewModel(app: Application) : BaseViewModel(app) {

    private var _contactId = MutableLiveData<Long?>(null)
    val contactId: LiveData<Long?> = _contactId

    fun select(selectedContactId: Long?) {
        _contactId.value = selectedContactId
    }
}