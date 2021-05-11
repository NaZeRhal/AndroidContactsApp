package com.maxrzhe.contacts.repository

import com.maxrzhe.contacts.data.ContactFbIdResponse
import com.maxrzhe.contacts.data.ContactListResponse
import com.maxrzhe.contacts.data.ContactResponseItem
import com.maxrzhe.contacts.model.ContactMapping
import com.maxrzhe.contacts.remote.RemoteDataSourceImpl
import com.maxrzhe.contacts.remote.Result
import com.maxrzhe.contacts.remote.Status
import com.maxrzhe.core.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class ContactMainRepository private constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val dbRepository: DatabaseRepository
) {

    suspend fun findById(fbId: String?): Flow<Result<Contact>> {
        return flow {
            emit(Result.loading())
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

    private fun cachedContactsFlow(): Flow<Result<List<Contact>>> =
        dbRepository.findAll()
            .map { Result.success(it) }
            .catch { emit(Result.error(it.message)) }
            .flowOn(Dispatchers.IO)
            .onStart { emit(Result.loading()) }

    suspend fun add(contact: Contact): Flow<Result<Contact>> {
        return flow {
            emit(Result.loading())
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
                    dbRepository.add(newContact)
                }
            }
            emit(Result.success(newContact))
        }.flowOn(Dispatchers.IO)
    }


    fun getContacts() = cachedContactsFlow().map { cachedContacts ->
        val result: Result<ContactListResponse> = remoteDataSource.getAllContacts()
        var fetchedContacts: List<Contact>? = null
        if (result.status == Status.SUCCESS) {
            result.data?.let {
                fetchedContacts = it.mapNotNull { entry ->
                    ContactMapping.contactRestToContact(entry.key, entry.value)
                }
                fetchedContacts?.let { contacts ->
                    cachedContacts.data?.let { cached ->
                        dbRepository.deleteByQuery(cached)
                    }
                    dbRepository.addAll(contacts)
                }
            }
        }
        Result.success(fetchedContacts)
    }
        .catch { emit(Result.error(it.message)) }
        .flowOn(Dispatchers.IO)
        .onStart { emit(Result.loading()) }

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