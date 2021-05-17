package com.maxrzhe.presentation.viewmodel.impl

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

class SharedViewModel(app: Application) : BaseViewModel(app) {

    private var _contactId = MutableLiveData<String?>(null)
    val contactId: LiveData<String?> = _contactId

    fun select(selectedContactId: String?) {
        _contactId.value = selectedContactId
    }
}