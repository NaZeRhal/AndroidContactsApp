package com.maxrzhe.contacts.remote

import com.maxrzhe.contacts.data.ContactFbIdResponse
import com.maxrzhe.contacts.data.ContactListResponse
import com.maxrzhe.contacts.data.ContactResponseItem
import com.maxrzhe.core.model.Contact

interface RemoteDataSource {

    suspend fun findById(fbId: String?): Result<ContactResponseItem>

    suspend fun add(contact: Contact): Result<ContactFbIdResponse>

//    suspend fun update(contact: Contact.Existing)
//
//    suspend fun delete(contact: Contact.Existing)

    suspend fun getAllContacts(): Result<ContactListResponse>
}