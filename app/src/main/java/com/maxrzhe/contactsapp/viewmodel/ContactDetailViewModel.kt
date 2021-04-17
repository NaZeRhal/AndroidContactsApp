package com.maxrzhe.contactsapp.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contactsapp.model.Contact
import com.maxrzhe.contactsapp.repository.Repository
import com.maxrzhe.contactsapp.repository.RepositoryFactory
import com.maxrzhe.contactsapp.repository.RepositoryType
import kotlinx.coroutines.launch

class ContactDetailViewModel(private val app: Application) :
    BaseViewModel(app) {

    private val repository: Repository = RepositoryFactory.create(app, RepositoryType.ROOM)

    private var _savedMarker = MutableLiveData(false)
    val savedMarker: LiveData<Boolean> = _savedMarker

    val id: ObservableField<Long?> = ObservableField(0)
    val name = ObservableField("")
    val email = ObservableField("")
    val phone = ObservableField("")
    val image = ObservableField("")

    fun manageSelectedId(selectedId: Long) {
            viewModelScope.launch {
                id.set(selectedId)
                val contact =
                    if (selectedId > 0) repository.findById(selectedId) else Contact.New()
                name.set(contact?.name ?: "")
                email.set(contact?.email ?: "")
                phone.set(contact?.phone ?: "")
                image.set(contact?.image ?: "")
            }
    }

    fun resetMarker() {
        _savedMarker.value = false
    }

    fun manageImageUri(imageUri: String) {
        image.set(imageUri)
    }

    private fun add(contact: Contact.New) {
        viewModelScope.launch {
            repository.add(contact)
        }
    }

    private fun update(contact: Contact.Existing) {
        viewModelScope.launch {
            repository.update(contact)
        }
    }

    fun addOrUpdate() {
        if (validateInput()) {
            if (id.get() != null && id.get()!! > 0) {
                val contact = Contact.Existing(
                    id = id.get() ?: 0,
                    name = name.get() ?: "",
                    phone = phone.get() ?: "",
                    email = email.get() ?: "",
                    image = image.get() ?: ""
                )
                update(contact)
            } else {
                val contact =
                    Contact.New(
                        name = name.get() ?: "",
                        phone = phone.get() ?: "",
                        email = email.get() ?: "",
                        image = image.get() ?: ""
                    )
                add(contact)
            }
            _savedMarker.value = true
        }
    }

    private fun validateInput(): Boolean {
        return when {
            name.get().isNullOrEmpty() -> {
                android.widget.Toast.makeText(
                    app.applicationContext,
                    "Please enter a name",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                false
            }
            !android.util.Patterns.PHONE.matcher(phone.get().toString()).matches() -> {
                android.widget.Toast.makeText(
                    app.applicationContext,
                    "Please enter correct phone number",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email.get().toString()).matches() -> {
                android.widget.Toast.makeText(
                    app.applicationContext,
                    "Please enter correct email",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> true
        }
    }
}