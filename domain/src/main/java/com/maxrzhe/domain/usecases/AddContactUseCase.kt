package com.maxrzhe.domain.usecases

import com.maxrzhe.common.util.Resource
import com.maxrzhe.domain.model.Contact
import com.maxrzhe.domain.repositories.ContactRepository
import kotlinx.coroutines.flow.Flow

class AddContactUseCase(private val contactRepository: ContactRepository) :
    UseCaseWithParams<Contact, Flow<Resource<Contact>>>() {
    override suspend fun buildUseCase(contact: Contact): Flow<Resource<Contact>> {
        return contactRepository.add(contact)
    }
}