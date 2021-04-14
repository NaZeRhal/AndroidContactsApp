package com.maxrzhe.contactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.maxrzhe.contactsapp.R
import com.maxrzhe.contactsapp.model.Contact
import com.maxrzhe.contactsapp.repository.Repository
import com.maxrzhe.contactsapp.repository.RepositoryFactory
import com.maxrzhe.contactsapp.repository.RepositoryType
import kotlinx.coroutines.launch

class ContactDetailViewModel(app: Application) : BaseViewModel(app) {

    private val repository: Repository = RepositoryFactory.create(app, RepositoryType.ROOM)

    fun add(contact: Contact) {
        viewModelScope.launch {
            repository.add(contact)
        }
    }

    fun update(contact: Contact) {
        viewModelScope.launch {
            repository.update(contact)
        }
    }

    fun detailImageText(
        contact: Contact?,
    ): Int {
        return if (contact == null) {
            R.string.detail_tv_add_image_text
        } else {
            R.string.detail_tv_change_image_text
        }
    }

    fun detailSubmitButtonText(
        contact: Contact?,
    ): Int {
        return if (contact == null) {
            R.string.detail_button_add_text
        } else {
            R.string.detail_button_save_changes_text
        }
    }
}