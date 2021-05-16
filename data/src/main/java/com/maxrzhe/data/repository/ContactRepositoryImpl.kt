package com.maxrzhe.data.repository

import android.app.Application
import androidx.room.withTransaction
import com.maxrzhe.common.util.Resource
import com.maxrzhe.data.app.ContactsApp
import com.maxrzhe.data.entities.ContactMapping
import com.maxrzhe.data.entities.api.ContactFbIdResponse
import com.maxrzhe.data.entities.api.ContactResponseItem
import com.maxrzhe.data.entities.room.ContactRoom
import com.maxrzhe.data.local.room.ContactRoomDatabase
import com.maxrzhe.data.remote.ContactsApi
import com.maxrzhe.data.remote.getResponse
import com.maxrzhe.data.remote.networkBoundResource
import com.maxrzhe.domain.model.Contact
import com.maxrzhe.domain.repositories.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ContactRepositoryImpl private constructor(app: Application) : ContactRepository {

    private val contactApi: ContactsApi = (app as ContactsApp).contactsApi
    private val db: ContactRoomDatabase = ContactRoomDatabase.getInstance(app)
    private val contactDao = db.contactDao()

    override fun getContacts(): Flow<Resource<List<Contact>>> = networkBoundResource(
        query = { contactDao.findAll().map { ContactMapping.contactRoomToContact(it) } },
        fetch = {
            getResponse(
                request = { contactApi.fetchContactsList() },
                defaultErrorMessage = "Error fetching contacts"
            )
        },
        saveFetchResult = { response ->
            response.data?.let { data ->
                db.withTransaction {
                    contactDao.deleteAll()
                    contactDao.addAll(data.map {
                        ContactMapping.contactRestToContactRoom(
                            it.key,
                            it.value
                        )
                    })
                }
            }
        }
    )

    override fun findById(fbId: String): Flow<Resource<Contact>> = networkBoundResource(
        query = {
            contactDao.findById(fbId)
                .map { ContactMapping.contactRoomToContact(it) }
        },
        fetch = {
            getResponse(
                request = { contactApi.fetchContactById(fbId) },
                defaultErrorMessage = "Error fetching contact by id"
            )
        },
        saveFetchResult = {}
    )

    override fun add(contact: Contact): Flow<Resource<Contact>> {
        return flow<Resource<Contact>> {
            emit(Resource.Loading())
            val result: Resource<ContactFbIdResponse> = getResponse(
                request = { contactApi.addContact(contact) },
                defaultErrorMessage = "Error adding contact to remote data source"
            )
            var emittingContact: Contact
            if (result is Resource.Success.Data) {
                result.data.let {
                    val newContact = ContactRoom(
                        id = 0,
                        fbId = it.fbId,
                        name = contact.name,
                        email = contact.email,
                        phone = contact.phone,
                        image = contact.image,
                        birthDate = contact.birthDate,
                        isFavorite = contact.isFavorite
                    )
                    contactDao.add(newContact)
                    emittingContact = ContactMapping.contactRoomToContact(newContact)
                }
                emit(Resource.Success.Data(emittingContact))
            } else {
                emit(
                    Resource.Error(Throwable("Unable to add contact to remote data source"))
                )
            }

        }.flowOn(Dispatchers.IO)
    }

    override fun update(contact: Contact): Flow<Resource<Contact>> = flow {
        emit(Resource.Loading())
        val result: Resource<ContactResponseItem> = getResponse(
            request = { contactApi.updateContact(contact.fbId, contact) },
            defaultErrorMessage = "Error updating contact ${contact.fbId}"
        )
        if (result is Resource.Success.Data) {
            result.data.let {
                db.withTransaction {
                    contactDao.deleteByFbIds(listOf(contact.fbId))
                    contactDao.add(ContactMapping.contactRestToContactRoom(contact.fbId, it))
                }
            }
            emit(Resource.Success.Data(contact))
        } else {
            emit(Resource.Error<Contact>(Throwable("Error updating contact in remote source")))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun delete(contact: Contact) {
        val result = getResponse(
            request = { contactApi.deleteContact(contact.fbId) },
            defaultErrorMessage = "Error deleting contact ${contact.fbId}"
        )
        if (result is Resource.Success) {
            contactDao.deleteByFbIds(listOf(contact.fbId))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ContactRepositoryImpl? = null

        fun getInstance(
            app: Application
        ): ContactRepositoryImpl {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = ContactRepositoryImpl(app)
                INSTANCE = instance
                return instance
            }
        }
    }
}