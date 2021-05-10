package com.maxrzhe.contacts.dao

import androidx.room.*
import com.maxrzhe.contacts.model.ContactRoom

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(contactRoom: ContactRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(contacts: List<ContactRoom>)

    @Update
    fun update(contactRoom: ContactRoom)

    @Delete
    fun delete(contactRoom: ContactRoom)

    @Query("DELETE FROM contacts_table WHERE fbId IN (:fbIds)")
    fun deleteByFbIds(fbIds: List<String>)

    @Query("SELECT * FROM contacts_table WHERE fbId=:fbId")
    fun findById(fbId: String): ContactRoom

    @Query("SELECT * FROM contacts_table")
    fun findAll(): List<ContactRoom>

}