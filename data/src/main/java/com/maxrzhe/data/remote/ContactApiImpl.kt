package com.maxrzhe.data.remote

import com.example.data_api.model.Contact
import com.maxrzhe.data.entities.api.ContactFbIdResponse
import com.maxrzhe.data.entities.api.ContactListResponse
import com.maxrzhe.data.entities.api.ContactResponseItem
import retrofit2.Response

class ContactApiImpl(private val contactService: ContactService) : ContactApi {
    override suspend fun fetchContactsList(): Response<ContactListResponse> {
        return contactService.fetchContactsList()
    }

    override suspend fun addContact(contact: Contact): Response<ContactFbIdResponse> {
        return contactService.addContact(contact)
    }

    override suspend fun updateContact(
        fbId: String,
        contact: Contact
    ): Response<ContactResponseItem> {
        return contactService.updateContact(fbId, contact)
    }

    override suspend fun fetchContactById(fbId: String): Response<ContactResponseItem> {
        return contactService.fetchContactById(fbId)
    }

    override suspend fun deleteContact(fbId: String): Response<Any> {
        return contactService.deleteContact(fbId)
    }
}