package com.maxrzhe.contactsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maxrzhe.contactsapp.dao.ContactDao
import com.maxrzhe.contactsapp.model.ContactRoom

@Database(entities = [ContactRoom::class], version = 1, exportSchema = false)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {
        private const val DATABASE_NAME = "contacts_db"

        @Volatile
        private var INSTANCE: ContactDatabase? = null

        fun getRoomDatabase(context: Context): ContactDatabase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, ContactDatabase::class.java, DATABASE_NAME)
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}