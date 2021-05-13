package com.maxrzhe.contacts.remote

import com.maxrzhe.contacts.data.ContactFbIdResponse
import com.maxrzhe.contacts.data.ContactListResponse
import com.maxrzhe.contacts.data.ContactResponseItem
import com.maxrzhe.core.model.Contact
import retrofit2.Response
import retrofit2.http.*

interface ContactsApi {

    @GET("contacts_db.json")
    suspend fun fetchContactsList(): Response<ContactListResponse>

    @POST("contacts_db.json")
    suspend fun addContact(@Body contact: Contact): Response<ContactFbIdResponse>

    @PUT("contacts_db/{fbId}/.json")
    suspend fun updateContact(
        @Path("fbId") fbId: String,
        @Body contact: Contact
    ): Response<ContactResponseItem>

    @GET("contacts_db/{fbId}.json")
    suspend fun fetchContactById(@Path("fbId") fbId: String): Response<ContactResponseItem>

    @DELETE("contacts_db/{fbId}.json")
    suspend fun deleteContact(@Path("fbId") fbId: String): Response<Any>
}