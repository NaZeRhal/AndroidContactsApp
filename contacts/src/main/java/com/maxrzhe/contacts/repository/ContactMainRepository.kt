package com.maxrzhe.contacts.repository

import android.util.Log
import com.maxrzhe.contacts.data.ContactFbIdResponse
import com.maxrzhe.contacts.data.ContactListResponse
import com.maxrzhe.contacts.data.ContactResponseItem
import com.maxrzhe.contacts.model.ContactMapping
import com.maxrzhe.contacts.remote.RemoteDataSourceImpl
import com.maxrzhe.contacts.remote.Result
import com.maxrzhe.contacts.remote.Status
import com.maxrzhe.core.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ContactMainRepository private constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val dbRepository: DatabaseRepository
) {

    suspend fun findById(fbId: String?): Flow<Result<Contact>> {
        return flow {
            emit(Result.loading())
            Log.i("SVC", "findByFlow: kuku")
            val result: Result<ContactResponseItem> = remoteDataSource.findById(fbId)
            var contact: Contact? = null
            if (result.status == Status.SUCCESS) {
                result.data?.let {
                    contact = ContactMapping.contactRestToContact(fbId, it)
                }
            }
            emit(Result.success(contact))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun add(contact: Contact): Flow<Result<Contact>> {
        return flow {
            emit(Result.loading())
            Log.i("SVC", "addFlow: kuku")
            val result: Result<ContactFbIdResponse> = remoteDataSource.add(contact)
            var newContact: Contact? = null
            if (result.status == Status.SUCCESS) {
                result.data?.let {
                    newContact = Contact(
                        fbId = it.fbId,
                        name = contact.name,
                        email = contact.email,
                        phone = contact.phone,
                        image = contact.image,
                        birthDate = contact.birthDate,
                        isFavorite = contact.isFavorite
                    )
                    storeToDb(newContact)
                }
            }
            emit(Result.success(newContact))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getContacts(): Flow<Result<List<Contact>>?> {
        return flow {
            emit(getCachedContacts())
            emit(Result.loading())
            val result: Result<ContactListResponse> = remoteDataSource.getAllContacts()
            var fetchedContacts: List<Contact>? = null
            if (result.status == Status.SUCCESS) {
                result.data?.let {
                    for (c in it) {
                        Log.i("DBG", "getContacts: ${c.key} ${c.value.name}")
                    }
                    fetchedContacts = it.mapNotNull { entry ->
                        ContactMapping.contactRestToContact(entry.key, entry.value)
                    }
                    fetchedContacts?.let { contacts ->
                        dbRepository.deleteByQuery(contacts)
                        dbRepository.addAll(contacts)
                    }
                }
            }
            for (c in fetchedContacts ?: emptyList()) {
                Log.i("DBG", "fetchedContacts: ${c.name}")
            }
            emit(Result.success(fetchedContacts))
        }.flowOn(Dispatchers.IO)
    }

    private fun getCachedContacts(): Result<List<Contact>>? =
        dbRepository.findAll()?.let {
            for (c in it) {
                Log.i("DBG", "getCachedContacts: ${c.name}")
            }
            Result.success(it)
        }

    private fun storeToDb(contact: Contact?) {
        dbRepository.add(contact)
    }


    private fun List<Contact>?.exist(
        fbId: String,
    ): Contact? {
        return this?.firstOrNull { it.fbId == fbId }
    }

    suspend fun update(contact: Contact) {
        TODO("Not yet implemented")
    }

    suspend fun delete(contact: Contact) {
        TODO("Not yet implemented")
    }


    companion object {
        @Volatile
        private var INSTANCE: ContactMainRepository? = null

        fun getInstance(
            networkService: RemoteDataSourceImpl,
            dbRepository: DatabaseRepository
        ): ContactMainRepository {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = ContactMainRepository(networkService, dbRepository)
                INSTANCE = instance
                return instance
            }
        }
    }
}