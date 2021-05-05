package com.maxrzhe.contacts.api

import com.maxrzhe.contacts.data.ContactListResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ContactsApi {

    @GET("./contacts_db.json")
    fun getContactsList(): Single<ContactListResponse>

}