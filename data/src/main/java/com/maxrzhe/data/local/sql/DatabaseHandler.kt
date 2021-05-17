package com.maxrzhe.data.local.sql

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.maxrzhe.data.entities.sql.ContactSql

class DatabaseHandler private constructor(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Contacts_db"

        const val TABLE_CONTACTS = "Contacts_table"
        const val KEY_ID = "_id"
        const val KEY_FB_ID = "fbId"
        const val KEY_NAME = "name"
        const val KEY_PHONE = "phone"
        const val KEY_EMAIL = "email"
        const val KEY_IMAGE = "image"
        const val KEY_DATE = "date"
        const val KEY_FAVORITE = "favorite"

        @Volatile
        private var INSTANCE: DatabaseHandler? = null

        fun getInstance(context: Context): DatabaseHandler {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = DatabaseHandler(context)
                INSTANCE = instance
                return instance
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE $TABLE_CONTACTS(" +
                "$KEY_ID INTEGER PRIMARY KEY, " +
                "$KEY_FB_ID TEXT, " +
                "$KEY_NAME TEXT, " +
                "$KEY_EMAIL TEXT, " +
                "$KEY_PHONE TEXT, " +
                "$KEY_IMAGE TEXT, " +
                "$KEY_DATE TEXT, " +
                "$KEY_FAVORITE INTEGER)"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun add(contact: ContactSql): Long {
        val db = this.writableDatabase
        val contentValues = getContentValue(contact)

        val success = db.insert(
            TABLE_CONTACTS,
            null,
            contentValues
        )
        db.close()
        return success
    }

    fun update(contact: ContactSql): Int {
        val db = this.writableDatabase
        val contentValues = getContentValue(contact)

        val success = db.update(
            TABLE_CONTACTS,
            contentValues,
            "$KEY_ID=${contact.id}",
            null
        )
        db.close()
        return success
    }

    fun delete(contact: ContactSql): Int {
        val db = this.writableDatabase

        val success = db.delete(
            TABLE_CONTACTS,
            "$KEY_ID=${contact.id}",
            null
        )
        db.close()
        return success
    }

    fun findAll(): List<ContactSql> {
        var contacts: List<ContactSql> = emptyList()
        val query = "SELECT * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(query, null)

            cursor?.let { raw ->
                with(raw) {
                    if (moveToFirst()) {
                        do {
                            val contact = ContactSql(
                                id = getLong(getColumnIndex(KEY_ID)),
                                fbId = getString(getColumnIndex(KEY_FB_ID)),
                                name = getString(getColumnIndex(KEY_NAME)),
                                phone = getString(getColumnIndex(KEY_PHONE)),
                                email = getString(getColumnIndex(KEY_EMAIL)),
                                image = getString(getColumnIndex(KEY_IMAGE)),
                                birthDate = getString(getColumnIndex(KEY_DATE)),
                                isFavorite = getInt(getColumnIndex(KEY_FAVORITE)) == 1
                            )
                            contacts = contacts + listOf(contact)
                        } while (moveToNext())
                    }
                }
            }
            db.close()
            return contacts
        } catch (e: SQLException) {
            db.execSQL(query)
            db.close()
            return emptyList()
        }
    }

    private fun getContentValue(contact: ContactSql): ContentValues {
        val contentValues = ContentValues()

        with(contact) {
            contentValues.apply {
                put(KEY_NAME, name)
                put(KEY_PHONE, phone)
                put(KEY_FB_ID, fbId)
                put(KEY_EMAIL, email)
                put(KEY_IMAGE, image)
                put(KEY_DATE, birthDate)
                put(KEY_FAVORITE, if (isFavorite) 1 else 0)
            }
        }
        return contentValues
    }

    fun findById(fbId: String): ContactSql? {
        val query = "SELECT * FROM $TABLE_CONTACTS WHERE $KEY_FB_ID=$fbId"

        val db = this.readableDatabase
        val cursor: Cursor?
        var contact: ContactSql? = null

        try {
            cursor = db.rawQuery(query, null)

            cursor?.let { raw ->
                with(raw) {
                    if (moveToFirst()) {
                        do {
                            contact = ContactSql(
                                id = getLong(getColumnIndex(KEY_ID)),
                                fbId = getString(getColumnIndex(KEY_FB_ID)),
                                name = getString(getColumnIndex(KEY_NAME)),
                                phone = getString(getColumnIndex(KEY_PHONE)),
                                email = getString(getColumnIndex(KEY_EMAIL)),
                                image = getString(getColumnIndex(KEY_IMAGE)),
                                birthDate = getString(getColumnIndex(KEY_DATE)),
                                isFavorite = getInt(getColumnIndex(KEY_FAVORITE)) == 1
                            )
                        } while (moveToNext())
                    }
                }
            }
            db.close()
            return contact
        } catch (e: SQLException) {
            db.execSQL(query)
            db.close()
            return contact
        }
    }
}