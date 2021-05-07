package com.maxrzhe.contacts.api

import com.maxrzhe.contacts.data.ContactFbIdResponse
import com.maxrzhe.contacts.data.ContactListResponse
import com.maxrzhe.core.model.Contact
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ContactsApi {

    @GET("contacts_db.json")
    fun getContactsListAsync(): Deferred<ContactListResponse>

    @POST("contacts_db.json")
    fun postContactAsync(@Body contact: Contact.New) : Deferred<ContactFbIdResponse>

}