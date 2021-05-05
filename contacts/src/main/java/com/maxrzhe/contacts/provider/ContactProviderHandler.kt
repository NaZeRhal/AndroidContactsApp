package com.maxrzhe.contacts.provider

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxrzhe.contacts.database.DatabaseHandler.Companion.KEY_DATE
import com.maxrzhe.contacts.database.DatabaseHandler.Companion.KEY_EMAIL
import com.maxrzhe.contacts.database.DatabaseHandler.Companion.KEY_FAVORITE
import com.maxrzhe.contacts.database.DatabaseHandler.Companion.KEY_ID
import com.maxrzhe.contacts.database.DatabaseHandler.Companion.KEY_IMAGE
import com.maxrzhe.contacts.database.DatabaseHandler.Companion.KEY_NAME
import com.maxrzhe.contacts.database.DatabaseHandler.Companion.KEY_PHONE
import com.maxrzhe.contacts.database.DatabaseHandler.Companion.TABLE_CONTACTS
import com.maxrzhe.contacts.repository.Repository
import com.maxrzhe.core.model.Contact

class ContactProviderHandler(private val context: Context) : Repository {

    private val allContacts = MutableLiveData<List<Contact.Existing>>()

    companion object {
        private const val CONTENT_URI =
            "content://com.maxrzhe.contacts.provider.ContactContentProvider/$TABLE_CONTACTS"
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

    override suspend fun findById(id: Long): Contact.Existing? {
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
                            image = getString(getColumnIndex(KEY_IMAGE)),
                            birthDate = getString(getColumnIndex(KEY_DATE)),
                            isFavorite = getInt(getColumnIndex(KEY_FAVORITE)) == 1
                        )
                    }
                }
            }
            return contact
        } catch (e: SQLException) {
            return null
        }
    }

    override suspend fun findAll(): LiveData<List<Contact.Existing>> {
        if (allContacts.value == null) {
            allContacts.value = loadAllContacts()
        }
        return allContacts
    }

    override suspend fun add(contact: Contact.New) {
        TODO("Not yet implemented")
    }

    override suspend fun update(contact: Contact.Existing) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(contact: Contact.Existing) {
        TODO("Not yet implemented")
    }


}