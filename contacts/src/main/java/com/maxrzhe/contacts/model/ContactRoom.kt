package com.maxrzhe.contacts.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "contacts_table")
data class ContactRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val email: String,
    val image: String,
    val birthDate: String,
    val isFavorite: Boolean
) : Parcelable