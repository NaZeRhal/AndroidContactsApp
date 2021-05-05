package com.maxrzhe.contacts.data


import com.google.gson.annotations.SerializedName
class ContactListResponse : ArrayList<ContactListResponseItem>()

data class ContactListResponseItem(
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