package com.maxrzhe.contacts.data

class ContactListResponse : HashMap<String, ContactListResponseItem>()

data class ContactListResponseItem(
    val name: String,
    val email: String,
    val phone: String,
    val image: String,
    val birthDate: String,
    val isFavorite: Boolean
)

data class ContactFbIdResponse(val name: String)