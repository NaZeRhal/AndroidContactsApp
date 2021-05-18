package com.maxrzhe.data.remote

import com.maxrzhe.data.entities.api.ContactFbIdResponse
import com.maxrzhe.data.entities.api.ContactListResponse
import com.maxrzhe.data.entities.api.ContactResponseItem
import com.example.data_api.model.Contact
import retrofit2.Response

interface ContactApi {

    suspend fun fetchContactsList(): Response<ContactListResponse>

    suspend fun addContact(contact: Contact): Response<ContactFbIdResponse>

    suspend fun updateContact(fbId: String, contact: Contact): Response<ContactResponseItem>

    suspend fun fetchContactById(fbId: String): Response<ContactResponseItem>

    suspend fun deleteContact(fbId: String): Response<Any>
}