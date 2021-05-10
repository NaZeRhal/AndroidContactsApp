package com.maxrzhe.contacts.remote

import com.maxrzhe.contacts.data.ContactFbIdResponse
import com.maxrzhe.contacts.data.ContactListResponse
import com.maxrzhe.contacts.data.ContactResponseItem
import com.maxrzhe.core.model.Contact
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ContactsApi {

    @GET("contacts_db.json")
    suspend fun fetchContactsList(): Response<ContactListResponse>

    @POST("contacts_db.json")
    suspend fun addContact(@Body contact: Contact) : Response<ContactFbIdResponse>

    @GET("contacts_db/{fbId}.json")
    suspend fun fetchContactById(@Path("fbId") fbId: String?): Response<ContactResponseItem>
}