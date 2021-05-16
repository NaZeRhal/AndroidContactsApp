package com.maxrzhe.data.provider

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.domain.model.Contact
import com.maxrzhe.data.local.sql.DatabaseHandler.Companion.KEY_DATE
import com.maxrzhe.data.local.sql.DatabaseHandler.Companion.KEY_EMAIL
import com.maxrzhe.data.local.sql.DatabaseHandler.Companion.KEY_FAVORITE
import com.maxrzhe.data.local.sql.DatabaseHandler.Companion.KEY_FB_ID
import com.maxrzhe.data.local.sql.DatabaseHandler.Companion.KEY_IMAGE
import com.maxrzhe.data.local.sql.DatabaseHandler.Companion.KEY_NAME
import com.maxrzhe.data.local.sql.DatabaseHandler.Companion.KEY_PHONE
import com.maxrzhe.data.local.sql.DatabaseHandler.Companion.TABLE_CONTACTS

class ContactProviderHandler(private val context: Context) {

    private val allContacts = MutableLiveData<List<Contact>>()

    companion object {
        private const val CONTENT_URI =
            "content://com.maxrzhe.data.provider.ContactContentProvider/$TABLE_CONTACTS"
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
                                fbId = getString(getColumnIndex(KEY_FB_ID)),
                                name = getString(getColumnIndex(KEY_NAME)),
                                phone = getString(getColumnIndex(KEY_PHONE)),
                                email = getString(getColumnIndex(KEY_EMAIL)),
                                image = getString(getColumnIndex(KEY_IMAGE)),
                                birthDate = getString(getColumnIndex(KEY_DATE)),
                                isFavorite = getInt(getColumnIndex(KEY_FAVORITE)) == 1
                            )
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


    fun findAll(): LiveData<List<Contact>> {
        if (allContacts.value == null) {
            allContacts.value = loadAllContacts()
        }
        return allContacts
    }
}