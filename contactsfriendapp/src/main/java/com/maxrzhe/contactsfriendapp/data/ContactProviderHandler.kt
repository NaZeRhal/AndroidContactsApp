package com.maxrzhe.contactsfriendapp.data

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.contactsfriendapp.model.Contact

class ContactProviderHandler(private val context: Context) {

    private val allContacts = MutableLiveData<List<Contact>>()

    companion object {
        private const val TABLE_CONTACTS = "contacts_table"
        private const val CONTENT_URI =
            "content://com.maxrzhe.contactsapp.provider.ContactContentProvider/$TABLE_CONTACTS"

        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_PHONE = "phone"
        private const val KEY_EMAIL = "email"
        private const val KEY_IMAGE = "image"
    }

    fun readAllContacts(): LiveData<List<Contact>> {
        if (allContacts.value == null) {
            allContacts.value = loadAllContacts()
        }
        return allContacts
    }

    private fun loadAllContacts(): List<Contact> {
        var contacts: List<Contact> = emptyList()
        val cursor: Cursor? =
            context.contentResolver.query(Uri.parse(CONTENT_URI), null, null, null, null)
        try {
            cursor?.let { raw ->
                with(raw) {
                    if (moveToFirst()) {
                        do {
                            val contact = Contact(
                                id = getLong(getColumnIndex(KEY_ID)),
                                name = getString(getColumnIndex(KEY_NAME)),
                                phone = getString(getColumnIndex(KEY_PHONE)),
                                email = getString(getColumnIndex(KEY_EMAIL)),
                                image = getString(getColumnIndex(KEY_IMAGE))
                            )
                            Log.i("IMAGE_CON", "loadAllContacts: ${contact.image}")
                            contacts = contacts + listOf(contact)
                        } while (moveToNext())
                    }
                }
            }
            return contacts
        } catch (e: SQLException) {
            return emptyList()
        }
    }

    fun loadContact(id: Long): Contact? {
        var contact: Contact? = null
        val uri = ContentUris.withAppendedId(Uri.parse(CONTENT_URI), id)

        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        try {
            cursor?.let { raw ->
                with(raw) {
                    if (moveToFirst()) {
                        contact = Contact(
                            id = getLong(getColumnIndex(KEY_ID)),
                            name = getString(getColumnIndex(KEY_NAME)),
                            phone = getString(getColumnIndex(KEY_PHONE)),
                            email = getString(getColumnIndex(KEY_EMAIL)),
                            image = getString(getColumnIndex(KEY_IMAGE))
                        )
                    }
                }
            }
            return contact
        } catch (e: SQLException) {
            return null
        }
    }
}