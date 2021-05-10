package com.maxrzhe.contacts.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ContactSql(
    val id: Long,
    val fbId: String,
    val name: String,
    val email: String,
    val phone: String,
    val image: String = "",
    val birthDate: String = "",
    val isFavorite: Boolean = false
) : Parcelable