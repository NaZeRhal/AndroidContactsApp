package com.maxrzhe.data.entities.api

import com.google.gson.annotations.SerializedName

class ContactListResponse : HashMap<String, ContactResponseItem>()

data class ContactResponseItem(
    val name: String,
    val email: String,
    val phone: String,
    val image: String,
    val birthDate: String,
    val isFavorite: Boolean
)

data class ContactFbIdResponse(
    @SerializedName("name")
    val fbId: String)