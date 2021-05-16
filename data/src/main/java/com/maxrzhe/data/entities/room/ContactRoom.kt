package com.maxrzhe.data.entities.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "contacts_table")
data class ContactRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val fbId: String,
    val name: String,
    val phone: String,
    val email: String,
    val image: String,
    val birthDate: String = "",
    val isFavorite: Boolean = false
) : Parcelable