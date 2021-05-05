package com.maxrzhe.contacts.api

import com.maxrzhe.contacts.data.ContactListResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ContactsApi {

    @GET("contacts_db.json")
    fun getContactsListAsync(): Deferred<ContactListResponse>

}