package com.maxrzhe.contactsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Contact {
    abstract val id: Long
    abstract val name: String
    abstract val email: String
    abstract val phone: String
    abstract val image: String

    @Parcelize
    data class New(
        override val id: Long = 0,
        override val name: String = "",
        override val email: String = "",
        override val phone: String = "",
        override val image: String = ""
    ) : Contact(), Parcelable

    @Parcelize
    data class Existing(
        override val id: Long,
        override val name: String,
        override val email: String,
        override val phone: String,
        override val image: String
    ) : Contact(), Parcelable
}
