package com.maxrzhe.domain.usecases

import com.maxrzhe.common.util.Resource
import com.maxrzhe.domain.model.Contact
import com.maxrzhe.domain.repositories.ContactRepository
import kotlinx.coroutines.flow.Flow

class GetContactsUseCase(private val repository: ContactRepository) :
    UseCase<Flow<Resource<List<Contact>>>>() {
    override suspend fun buildUseCase(): Flow<Resource<List<Contact>>> {
        return repository.getContacts()
    }
}