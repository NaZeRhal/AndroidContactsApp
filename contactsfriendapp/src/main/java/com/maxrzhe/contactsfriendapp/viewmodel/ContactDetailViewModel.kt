package com.maxrzhe.contactsfriendapp.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contactsfriendapp.data.ContactProviderHandler
import com.maxrzhe.contactsfriendapp.model.Contact
import kotlinx.coroutines.launch

class ContactDetailViewModel(app: Application) :
    BaseViewModel(app) {

    private val providerHandler = ContactProviderHandler(app)
    private var id: Long? = null

    val name = ObservableField<String?>()
    val email = ObservableField<String?>()
    val phone = ObservableField<String?>()
    val image = ObservableField<String?>()
    val isLoading = ObservableBoolean(true)

    fun manageSelectedId(selectedId: Long?) {
        isLoading.set(true)
        id = selectedId
        viewModelScope.launch {
            val contact =
                if (selectedId != null) providerHandler.loadContact(selectedId) else null
            setupFields(contact)
            isLoading.set(false)
        }
    }

    private fun setupFields(contact: Contact?) {
        name.set(contact?.name)
        email.set(contact?.email)
        phone.set(contact?.phone)
        image.set(contact?.image)
    }
}