package com.maxrzhe.contactsapp.model

import java.io.Serializable

data class Contact(
    val id: Int,
    var name: String,
    var phone: String,
    var email: String,
    var image: String
): Serializable