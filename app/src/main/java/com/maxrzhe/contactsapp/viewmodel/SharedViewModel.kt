package com.maxrzhe.contactsapp.viewmodel

import android.app.Application
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contactsapp.R
import com.maxrzhe.contactsapp.model.Contact
import com.maxrzhe.contactsapp.repository.Repository
import com.maxrzhe.contactsapp.repository.RepositoryFactory
import com.maxrzhe.contactsapp.repository.RepositoryType
import kotlinx.coroutines.launch

class SharedViewModel(private val app: Application) : BaseViewModel(app) {

    private val repository: Repository = RepositoryFactory.create(app, RepositoryType.ROOM)

    private var _selectedItem = MutableLiveData<Contact?>(null)
    val selectedItem: LiveData<Contact?> = _selectedItem

    private var id: MutableLiveData<Long?> =
        Transformations.map(selectedItem) { contact -> contact?.id } as MutableLiveData<Long?>

    private var isNew: MutableLiveData<Boolean>

    @Bindable
    var name = MutableLiveData<String?>()

    @Bindable
    var email = MutableLiveData<String?>()

    @Bindable
    var phone = MutableLiveData<String?>()

    @Bindable
    var image = MutableLiveData<String?>()


    init {
        name =
            Transformations.map(selectedItem) { contact -> contact?.name } as MutableLiveData<String?>
        email =
            Transformations.map(selectedItem) { contact -> contact?.email } as MutableLiveData<String?>
        phone =
            Transformations.map(selectedItem) { contact -> contact?.phone } as MutableLiveData<String?>
        image =
            Transformations.map(selectedItem) { contact -> contact?.image } as MutableLiveData<String?>
        isNew =
            Transformations.map(selectedItem) { contact -> contact == null } as MutableLiveData<Boolean>

    }

    fun onSaveContact() {
        if (validateInput()) {
            val contact = Contact(
                id = id.value ?: 0,
                name = name.value,
                phone = phone.value,
                email = email.value,
                image = image.value
            )

            if (contact.id <= 0) {
                add(contact)
            } else {
                update(contact)
            }
        }
    }

    private fun validateInput(): Boolean {
        return when {
            name.value.isNullOrEmpty() -> {
                android.widget.Toast.makeText(
                    app.applicationContext,
                    "Please enter a name",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                false
            }
            !android.util.Patterns.PHONE.matcher(phone.value.toString()).matches() -> {
                android.widget.Toast.makeText(
                    app.applicationContext,
                    "Please enter correct phone number",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches() -> {
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

    fun select(selectedContact: Contact?) {
        _selectedItem.value = selectedContact
    }

    fun detailImageText(): Int {
        return if (isNew.value == null || isNew.value == true) {
            R.string.detail_tv_add_image_text
        } else {
            R.string.detail_tv_change_image_text
        }
    }

    fun detailSubmitButtonText(): Int {
        return if (id.value == null) {
            R.string.detail_button_add_text
        } else {
            R.string.detail_button_save_changes_text
        }
    }

    private fun add(contact: Contact) {
        viewModelScope.launch {
            repository.add(contact)
        }
    }

    private fun update(contact: Contact) {
        viewModelScope.launch {
            repository.update(contact)
        }
    }
}