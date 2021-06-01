package com.maxrzhe.presentation.viewmodel.impl.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

class SharedViewModel : BaseViewModel() {

    private var _contactId = MutableLiveData<String?>(null)
    val contactId: LiveData<String?> = _contactId

    fun select(selectedContactId: String?) {
        _contactId.value = selectedContactId
    }
}