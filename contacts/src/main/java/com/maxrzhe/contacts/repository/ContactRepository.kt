package com.maxrzhe.contacts.repository

import android.app.Application
import androidx.room.withTransaction
import com.maxrzhe.contacts.app.ContactsApp
import com.maxrzhe.contacts.data.ContactFbIdResponse
import com.maxrzhe.contacts.data.ContactResponseItem
import com.maxrzhe.contacts.database.ContactRoomDatabase
import com.maxrzhe.contacts.model.ContactMapping
import com.maxrzhe.contacts.model.ContactRoom
import com.maxrzhe.contacts.remote.ContactsApi
import com.maxrzhe.contacts.remote.Resource
import com.maxrzhe.contacts.remote.getResponse
import com.maxrzhe.contacts.remote.networkBoundResource
import com.maxrzhe.core.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ContactRepository private constructor(app: Application) {

    private val contactApi: ContactsApi = (app as ContactsApp).contactsApi
    private val db: ContactRoomDatabase = ContactRoomDatabase.getInstance(app)
    private val contactDao = db.contactDao()

    fun getContacts(): Flow<Resource<List<Contact>>> = networkBoundResource(
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

    fun findById(fbId: String) = networkBoundResource(
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

    fun add(contact: Contact): Flow<Resource<Contact>> {
        return flow<Resource<Contact>> {
            emit(Resource.Loading<Contact>())
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
                    Resource.Error<Contact>(Throwable("Unable to add contact to remote data source"))
                )
            }

        }.flowOn(Dispatchers.IO)
    }

    fun update(contact: Contact) = flow {
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

    suspend fun delete(contact: Contact) {
        withContext(Dispatchers.Main) {
            val result = getResponse(
                request = { contactApi.deleteContact(contact.fbId) },
                defaultErrorMessage = "Error deleting contact ${contact.fbId}"
            )
            withContext(Dispatchers.IO) {
                if (result is Resource.Success) {
                    contactDao.deleteByFbIds(listOf(contact.fbId))
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ContactRepository? = null

        fun getInstance(
            app: Application
        ): ContactRepository {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = ContactRepository(app)
                INSTANCE = instance
                return instance
            }
        }
    }
}