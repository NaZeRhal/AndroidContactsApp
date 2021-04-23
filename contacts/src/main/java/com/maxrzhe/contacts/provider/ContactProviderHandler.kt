package com.maxrzhe.contacts.provider

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.contacts.database.DatabaseHandler.Companion.KEY_EMAIL
import com.maxrzhe.contacts.database.DatabaseHandler.Companion.KEY_ID
import com.maxrzhe.contacts.database.DatabaseHandler.Companion.KEY_IMAGE
import com.maxrzhe.contacts.database.DatabaseHandler.Companion.KEY_NAME
import com.maxrzhe.contacts.database.DatabaseHandler.Companion.KEY_PHONE
import com.maxrzhe.contacts.database.DatabaseHandler.Companion.TABLE_CONTACTS
import com.maxrzhe.core.model.Contact

class ContactProviderHandler(private val context: Context) {

    private val allContacts = MutableLiveData<List<Contact.Existing>>()

    companion object {
        private const val CONTENT_URI =
            "content://com.maxrzhe.contacts.provider.ContactContentProvider/$TABLE_CONTACTS"
    }

    fun readAllContacts(): LiveData<List<Contact.Existing>> {
        if (allContacts.value == null) {
            allContacts.value = loadAllContacts()
        }
        return allContacts
    }

    private fun loadAllContacts(): List<Contact.Existing> {
        var contacts: List<Contact.Existing> = emptyList()
        val cursor: Cursor? =
            context.contentResolver.query(Uri.parse(CONTENT_URI), null, null, null, null)
        try {
            cursor?.let { raw ->
                with(raw) {
                    if (moveToFirst()) {
                        do {
                            val contact = Contact.Existing(
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

    fun loadContact(id: Long): Contact.Existing? {
        var contact: Contact.Existing? = null
        val uri = ContentUris.withAppendedId(Uri.parse(CONTENT_URI), id)

        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        try {
            cursor?.let { raw ->
                with(raw) {
                    if (moveToFirst()) {
                        contact = Contact.Existing(
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