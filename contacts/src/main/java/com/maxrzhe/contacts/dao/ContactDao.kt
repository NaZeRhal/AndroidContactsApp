package com.maxrzhe.contacts.dao

import androidx.room.*
import com.maxrzhe.contacts.model.ContactRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(contactRoom: ContactRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(contacts: List<ContactRoom>)

    @Delete
    fun delete(contactRoom: ContactRoom)

    @Query("DELETE FROM contacts_table")
    fun deleteAll()

    @Query("DELETE FROM contacts_table WHERE fbId IN (:fbIds)")
    suspend fun deleteByFbIds(fbIds: List<String>)

    @Query("SELECT * FROM contacts_table WHERE fbId=:fbId")
    fun findById(fbId: String): Flow<ContactRoom>

    @Query("SELECT * FROM contacts_table")
    fun findAll(): Flow<List<ContactRoom>>

}