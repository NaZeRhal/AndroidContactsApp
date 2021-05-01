package com.maxrzhe.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Contact {
    abstract val name: String
    abstract val email: String
    abstract val phone: String
    abstract val image: String
    abstract val birthDate: String

    @Parcelize
    data class New(
        override val name: String = "",
        override val email: String = "",
        override val phone: String = "",
        override val image: String = "",
        override val birthDate: String = ""
    ) : Contact(), Parcelable

    @Parcelize
    data class Existing(
        val id: Long,
        override val name: String,
        override val email: String,
        override val phone: String,
        override val image: String,
        override val birthDate: String = ""
    ) : Contact(), Parcelable
}
