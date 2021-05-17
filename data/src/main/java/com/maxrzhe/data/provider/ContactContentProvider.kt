package com.maxrzhe.data.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import com.maxrzhe.data.local.sql.DatabaseHandler
import com.maxrzhe.data.local.sql.DatabaseHandler.Companion.KEY_ID
import com.maxrzhe.data.local.sql.DatabaseHandler.Companion.TABLE_CONTACTS

class ContactContentProvider : ContentProvider() {

    private var database: SQLiteDatabase? = null
    private val projectionMap = HashMap<String, String>()

    companion object {
        private const val AUTHORITY =
            "com.maxrzhe.data.provider.ContactContentProvider"
        private const val CODE_CONTACTS = 111
        private const val CODE_CONTACT_ID = 222
        private const val URL = "content://$AUTHORITY/${TABLE_CONTACTS}"

        private fun contentUri() = Uri.parse(URL)
        private fun matcher() = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TABLE_CONTACTS, CODE_CONTACTS)
            addURI(AUTHORITY, "$TABLE_CONTACTS/#", CODE_CONTACT_ID)
        }
    }

    override fun onCreate(): Boolean {
        database = context?.let { DatabaseHandler.getInstance(it).readableDatabase }
        return database != null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val sqBuilder = SQLiteQueryBuilder()
        sqBuilder.tables = TABLE_CONTACTS

        when (matcher().match(uri)) {
            CODE_CONTACTS -> {
                sqBuilder.projectionMap = projectionMap
            }
            CODE_CONTACT_ID -> {
                sqBuilder.appendWhere("$KEY_ID=${uri.pathSegments[1]}")
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        val cursor: Cursor =
            sqBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder)
        context?.let { cursor.setNotificationUri(it.contentResolver, uri) }

        return cursor
    }

    override fun getType(uri: Uri): String {
        return when (matcher().match(uri)) {
            CODE_CONTACTS -> "vnd.android.cursor.dir/$AUTHORITY.$TABLE_CONTACTS"
            CODE_CONTACT_ID -> "vnd.android.cursor.item/$AUTHORITY.$TABLE_CONTACTS"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val rowId = database?.insert(TABLE_CONTACTS, "", values)
        if (rowId != null && rowId > 0) {
            val uriTmp = ContentUris.withAppendedId(contentUri(), rowId)
            notifyChanges(uriTmp)
            return uriTmp
        } else {
            throw SQLException("Failed to add a record into $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val count = when (matcher().match(uri)) {
            CODE_CONTACTS -> database?.delete(TABLE_CONTACTS, selection, selectionArgs)
            CODE_CONTACT_ID -> database?.delete(
                TABLE_CONTACTS,
                createSelection(uri, selection),
                selectionArgs
            )
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        notifyChanges(uri)
        return count ?: 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val count = when (matcher().match(uri)) {
            CODE_CONTACTS -> database?.update(TABLE_CONTACTS, values, selection, selectionArgs)
            CODE_CONTACT_ID -> database?.update(
                TABLE_CONTACTS,
                values,
                createSelection(uri, selection),
                selectionArgs
            )
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        notifyChanges(uri)
        return count ?: 0
    }

    private fun createSelection(uri: Uri, selection: String?): String {
        val id = uri.pathSegments[1]
        val whereSelection = if (!TextUtils.isEmpty(selection)) " AND ($selection)" else ""
        return "$KEY_ID=$id${whereSelection}"
    }

    private fun notifyChanges(uri: Uri) {
        context?.contentResolver?.notifyChange(uri, null)
    }
}