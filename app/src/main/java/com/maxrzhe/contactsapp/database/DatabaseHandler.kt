package com.maxrzhe.contactsapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.maxrzhe.contactsapp.model.Contact

class DatabaseHandler(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Contacts_db"
        private const val TABLE_CONTACTS = "Contacts_table"

        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_PHONE = "phone"
        private const val KEY_EMAIL = "email"
        private const val KEY_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE $TABLE_CONTACTS(" +
                "$KEY_ID INTEGER PRIMARY KEY, " +
                "$KEY_NAME TEXT, " +
                "$KEY_EMAIL TEXT, " +
                "$KEY_PHONE TEXT, " +
                "$KEY_IMAGE TEXT)"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun add(contact: Contact): Long {
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

    fun update(contact: Contact): Int {
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

    fun delete(contact: Contact): Int {
        val db = this.writableDatabase

        val success = db.delete(
            TABLE_CONTACTS,
            "$KEY_ID=${contact.id}",
            null
        )
        db.close()
        return success
    }

    fun findAll(): List<Contact> {
        var contacts: List<Contact> = emptyList()
        val query = "SELECT * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(query, null)

            cursor?.let { raw ->
                with(raw) {
                    if (moveToFirst()) {
                        do {
                            val contact = Contact(
                                id = getLong(getColumnIndex(KEY_ID)),
                                name = getString(getColumnIndex(KEY_NAME)),
                                phone = getString(getColumnIndex(KEY_PHONE)),
                                email = getString(getColumnIndex(KEY_EMAIL)),
                                image = getString(getColumnIndex(KEY_IMAGE))
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

    private fun getContentValue(contact: Contact): ContentValues {
        val contentValues = ContentValues()

        with(contact) {
            contentValues.apply {
                put(KEY_NAME, name)
                put(KEY_PHONE, phone)
                put(KEY_EMAIL, email)
                put(KEY_IMAGE, image)
            }
        }
        return contentValues
    }

}