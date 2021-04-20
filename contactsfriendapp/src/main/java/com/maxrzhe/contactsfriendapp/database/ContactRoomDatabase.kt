package com.maxrzhe.contactsfriendapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maxrzhe.contactsfriendapp.dao.ContactDao
import com.maxrzhe.contactsfriendapp.model.ContactRoom

@Database(entities = [ContactRoom::class], version = 1, exportSchema = false)
abstract class ContactRoomDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {
        private const val DATABASE_NAME = "contacts_db"

        @Volatile
        private var INSTANCE: ContactRoomDatabase? = null

        fun getInstance(context: Context): ContactRoomDatabase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, ContactRoomDatabase::class.java, DATABASE_NAME)
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}