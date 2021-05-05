package com.maxrzhe.contacts.rest

import com.google.gson.annotations.SerializedName

data class ContactsListResponse(val contacts_db: List<ContactsListItem>)

data class ContactsListItem(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    val image: String,
    @SerializedName("birthday")
    val birthDate: String,
    @SerializedName("favorite")
    val isFavorite: Boolean
)