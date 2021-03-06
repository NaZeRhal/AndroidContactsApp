package com.maxrzhe.data.remote

import com.example.data_api.model.Contact
import com.maxrzhe.data.entities.api.ContactFbIdResponse
import com.maxrzhe.data.entities.api.ContactListResponse
import com.maxrzhe.data.entities.api.ContactResponseItem
import retrofit2.Response
import retrofit2.http.*

interface ContactService {

    @GET("contacts_db.json")
    suspend fun fetchContactsList(): Response<ContactListResponse>

    @POST("contacts_db.json")
    suspend fun addContact(@Body contact: Contact): Response<ContactFbIdResponse>

    @PUT("contacts_db/{fbId}.json")
    suspend fun updateContact(
        @Path("fbId") fbId: String,
        @Body contact: Contact
    ): Response<ContactResponseItem>

    @GET("contacts_db/{fbId}.json")
    suspend fun fetchContactById(@Path("fbId") fbId: String): Response<ContactResponseItem>

    @DELETE("contacts_db/{fbId}.json")
    suspend fun deleteContact(@Path("fbId") fbId: String): Response<Any>
}