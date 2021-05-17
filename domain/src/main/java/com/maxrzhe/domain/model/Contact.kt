package com.maxrzhe.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val fbId: String,
    val name: String,
    val email: String,
    val phone: String,
    val image: String,
    val birthDate: String,
    val isFavorite: Boolean
) : Parcelable
