package com.maxrzhe.data.repository

import com.example.data_api.model.Contact
import com.example.data_api.repositories.ContactRepository
import com.maxrzhe.common.util.Resource
import com.maxrzhe.data.entities.ContactMapping
import com.maxrzhe.data.entities.api.ContactFbIdResponse
import com.maxrzhe.data.entities.api.ContactResponseItem
import com.maxrzhe.data.entities.room.ContactRoom
import com.maxrzhe.data.local.ContactDatabase
import com.maxrzhe.data.remote.ContactApi
import com.maxrzhe.data.remote.getResponse
import com.maxrzhe.data.remote.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class ContactRepositoryImpl(
    private val contactApi: ContactApi,
    private val contactDatabase: ContactDatabase
) : ContactRepository {

    override fun getContacts(): Flow<Resource<List<Contact>>> = networkBoundResource(
        query = { contactDatabase.findAll().map { ContactMapping.contactRoomToContact(it) } },
        fetch = {
            getResponse(
                request = { contactApi.fetchContactsList() },
                defaultErrorMessage = "Error fetching contacts"
            )
        },
        saveFetchResult = { response ->
            response.data?.let { data ->
                val contacts = data.map {
                    ContactMapping.contactRestToContactRoom(
                        it.key,
                        it.value
                    )
                }
                contactDatabase.refreshAll(contacts)
            }
        }
    )

    override fun findById(fbId: String): Flow<Resource<Contact>> =
        networkBoundResource(
            query = {
                contactDatabase.findById(fbId)
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
                    contactDatabase.add(newContact)
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

    override fun update(contact: Contact): Flow<Resource<Contact>> =
        flow {
            emit(Resource.Loading())
            val result: Resource<ContactResponseItem> = getResponse(
                request = { contactApi.updateContact(contact.fbId, contact) },
                defaultErrorMessage = "Error updating contact ${contact.fbId}"
            )
            if (result is Resource.Success.Data) {
                result.data.let {
                    val contactRoom = ContactMapping.contactRestToContactRoom(contact.fbId, it)
                    contactDatabase.refreshByIds(listOf(contact.fbId), listOf(contactRoom))
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
            contactDatabase.deleteByFbIds(listOf(contact.fbId))
        }
    }
}