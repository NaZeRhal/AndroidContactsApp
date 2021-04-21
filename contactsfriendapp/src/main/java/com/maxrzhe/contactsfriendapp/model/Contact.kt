package com.maxrzhe.contactsfriendapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    val image: String
): Parcelable