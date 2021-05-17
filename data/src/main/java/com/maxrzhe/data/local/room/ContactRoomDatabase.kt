package com.maxrzhe.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maxrzhe.data.entities.room.ContactRoom

@Database(entities = [ContactRoom::class], version = 1, exportSchema = false)
abstract class ContactRoomDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao
}