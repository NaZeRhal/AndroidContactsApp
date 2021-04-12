package com.maxrzhe.contactsapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "contacts_table")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String?,
    val phone: String?,
    val email: String?,
    val image: String?
) : Parcelable