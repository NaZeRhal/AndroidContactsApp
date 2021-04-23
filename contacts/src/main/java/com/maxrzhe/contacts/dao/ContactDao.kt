package com.maxrzhe.contacts.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.maxrzhe.contacts.model.ContactRoom

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(contactRoom: ContactRoom)

    @Update
    suspend fun update(contactRoom: ContactRoom)

    @Delete
    suspend fun delete(contactRoom: ContactRoom)

    @Query("SELECT * FROM contacts_table WHERE id=:id")
    suspend fun findById(id: Long): ContactRoom?

    @Query("SELECT * FROM contacts_table")
    fun findAll(): LiveData<List<ContactRoom>>

}