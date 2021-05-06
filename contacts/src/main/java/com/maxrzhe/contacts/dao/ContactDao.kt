package com.maxrzhe.contacts.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.maxrzhe.contacts.model.ContactRoom

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(contactRoom: ContactRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(contacts: List<ContactRoom>)

    @Update
    suspend fun update(contactRoom: ContactRoom)

    @Delete
    suspend fun delete(contactRoom: ContactRoom)

    @Delete
    suspend fun deleteAll(contacts: List<ContactRoom>)

    @Query("SELECT * FROM contacts_table WHERE fbId=:fbId")
    suspend fun findById(fbId: String): ContactRoom?

    @Query("SELECT * FROM contacts_table")
    fun findAll(): LiveData<List<ContactRoom>>

}