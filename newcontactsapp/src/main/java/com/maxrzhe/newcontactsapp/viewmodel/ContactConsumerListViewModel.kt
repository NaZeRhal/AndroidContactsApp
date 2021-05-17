package com.maxrzhe.newcontactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.maxrzhe.data.provider.ContactProviderHandler
import com.maxrzhe.domain.model.Contact
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

class ContactConsumerListViewModel(app: Application) : BaseViewModel(app) {

    private var readAllData: LiveData<List<Contact>>
    private val providerHandler = ContactProviderHandler(app)

    init {
            readAllData = providerHandler.findAll()
    }

    fun findAll(): LiveData<List<Contact>> {
        return readAllData
    }

}