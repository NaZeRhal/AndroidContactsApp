package com.maxrzhe.contacts.repository

import android.app.Application
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

class ContactMainRepository private constructor(app: Application) {

    private val remoteDataSource: RemoteDataSourceImpl = RemoteDataSourceImpl(app)
    private val dbRepository: DatabaseRepository = RoomRepositoryImpl.getInstance(app)

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

    fun getContacts() = dbRepository.findAll()
        .map { Result.success(it) }
        .map { cachedContacts ->
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
                Result.success(fetchedContacts)
            } else {
                Result.success(cachedContacts.data)
            }

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
            app: Application
        ): ContactMainRepository {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = ContactMainRepository(app)
                INSTANCE = instance
                return instance
            }
        }
    }
}