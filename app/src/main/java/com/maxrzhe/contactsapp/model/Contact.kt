package com.maxrzhe.contactsapp.model

import androidx.annotation.DrawableRes

data class Contact(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String,
    @DrawableRes
    val image: Int
)