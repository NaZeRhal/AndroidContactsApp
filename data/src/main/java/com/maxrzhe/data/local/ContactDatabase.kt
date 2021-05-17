package com.maxrzhe.data.local

import com.maxrzhe.data.entities.room.ContactRoom
import kotlinx.coroutines.flow.Flow

interface ContactDatabase {

    suspend fun add(contactRoom: ContactRoom)

    suspend fun addAll(contacts: List<ContactRoom>)

    fun delete(contactRoom: ContactRoom)

    fun deleteAll()

    suspend fun deleteByFbIds(fbIds: List<String>)

    fun findById(fbId: String): Flow<ContactRoom>

    fun findAll(): Flow<List<ContactRoom>>

    suspend fun refreshAll(contacts: List<ContactRoom>)

    suspend fun refreshByIds(ids: List<String>, contacts: List<ContactRoom>)
}