package com.maxrzhe.contacts.rest

import io.reactivex.Single
import retrofit2.http.GET

interface ContactsApi {

    @GET(".json")
    fun getContactsList(): Single<ContactsListResponse>

}