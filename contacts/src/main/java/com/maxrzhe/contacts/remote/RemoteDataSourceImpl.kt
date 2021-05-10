package com.maxrzhe.contacts.remote

import android.app.Application
import android.util.Log
import com.maxrzhe.contacts.app.ContactsApp
import com.maxrzhe.contacts.data.ContactFbIdResponse
import com.maxrzhe.contacts.data.ContactListResponse
import com.maxrzhe.contacts.data.ContactResponseItem
import com.maxrzhe.core.model.Contact
import retrofit2.Response

class RemoteDataSourceImpl(app: Application) : RemoteDataSource {

    private val contactsApi = (app as ContactsApp).contactsApi

    override suspend fun findById(fbId: String?): Result<ContactResponseItem> {
        return getResponse(
            request = {contactsApi.fetchContactById(fbId)},
            defaultErrorMessage = "Error fetching contact by id"
        )
    }


    override suspend fun add(contact: Contact): Result<ContactFbIdResponse> {
        return getResponse(
            request = {
                Log.i("SVC", "addApi: $contact")
                contactsApi.addContact(contact)
            },
            defaultErrorMessage = "Error adding contact to remote data source"
        )
    }

    override suspend fun getAllContacts(): Result<ContactListResponse> {
        return getResponse(
            request = { contactsApi.fetchContactsList() },
            defaultErrorMessage = "Error fetching contacts list"
        )
    }

    private suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String
    ): Result<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                Result.success(result.body())
            } else {
                Result.error(result.errorBody()?.toString() ?: defaultErrorMessage)
            }
        } catch (e: Throwable) {
            Result.error(e.message)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: RemoteDataSourceImpl? = null

        fun getInstance(
            app: Application
        ): RemoteDataSourceImpl {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = RemoteDataSourceImpl(app)
                INSTANCE = instance
                return instance
            }
        }
    }
}