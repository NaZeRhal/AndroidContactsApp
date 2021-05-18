package com.maxrzhe.domain.usecases

import com.maxrzhe.common.util.Resource
import com.example.data_api.model.Contact
import com.example.data_api.repositories.ContactRepository
import kotlinx.coroutines.flow.Flow

class GetContactsUseCase(private val repository: ContactRepository) :
    UseCase<Flow<Resource<List<Contact>>>>() {
    override suspend fun buildUseCase(): Flow<Resource<List<Contact>>> {
        return repository.getContacts()
    }
}