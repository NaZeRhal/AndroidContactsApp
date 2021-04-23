package com.maxrzhe.contacts.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contacts.R
import com.maxrzhe.contacts.repository.Repository
import com.maxrzhe.contacts.repository.RepositoryFactory
import com.maxrzhe.contacts.repository.RepositoryType
import com.maxrzhe.core.model.Contact
import kotlinx.coroutines.launch

class ContactDetailViewModel(private val app: Application) :
    com.maxrzhe.core.viewmodel.BaseViewModel(app) {

    private val repository: Repository = RepositoryFactory.create(app, RepositoryType.PLAIN_SQL)
//    private val repository: Repository = RepositoryFactory.create(app, RepositoryType.ROOM)

    private var _savedMarker = MutableLiveData(false)
    val savedMarker: LiveData<Boolean> = _savedMarker

    private var id: Long? = null

    val name = ObservableField<String?>()
    val email = ObservableField<String?>()
    val phone = ObservableField<String?>()
    val image = ObservableField<String?>()
    val isLoading = ObservableBoolean(true)

    val imageTextRes = ObservableInt(R.string.detail_tv_add_image_text)
    val buttonTextRes = ObservableInt(R.string.detail_button_add_text)

    fun manageSelectedId(selectedId: Long?) {
        isLoading.set(true)
        id = selectedId
        viewModelScope.launch {
            val contact =
                if (selectedId != null) repository.findById(selectedId) else Contact.New()
            setupFields(contact)
            isLoading.set(false)
        }
    }

    private fun setupFields(contact: Contact?) {
        if (contact == null || contact is Contact.New) {
            name.set("")
            email.set("")
            phone.set("")
            image.set("")
            imageTextRes.set(R.string.detail_tv_add_image_text)
            buttonTextRes.set(R.string.detail_button_add_text)
        } else {
            name.set(contact.name)
            email.set(contact.email)
            phone.set(contact.phone)
            image.set(contact.image)
            imageTextRes.set(R.string.detail_tv_change_image_text)
            buttonTextRes.set(R.string.detail_button_save_changes_text)
        }
    }

    fun resetMarker() {
        _savedMarker.value = false
    }

    fun manageImageUri(imageUri: String) {
        image.set(imageUri)
        imageTextRes.set(R.string.detail_tv_change_image_text)
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
            if (id != null) {
                id?.let {
                    val contact = Contact.Existing(
                        id = it,
                        name = name.get() ?: "",
                        phone = phone.get() ?: "",
                        email = email.get() ?: "",
                        image = image.get() ?: ""
                    )
                    update(contact)
                }
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