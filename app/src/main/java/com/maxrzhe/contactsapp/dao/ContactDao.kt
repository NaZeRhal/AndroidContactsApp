package com.maxrzhe.contactsapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.maxrzhe.contactsapp.model.Contact

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(contact: Contact)

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Query("SELECT * FROM contacts_table")
    fun findAll(): LiveData<List<Contact>>

}