package com.maxrzhe.domain.usecases

import com.maxrzhe.domain.model.Contact
import com.maxrzhe.domain.repositories.ContactRepository

class DeleteContactUseCase(private val contactRepository: ContactRepository) :
    UseCaseWithParams<Contact, Unit>() {
    override suspend fun buildUseCase(contact: Contact) {
        contactRepository.delete(contact)
    }

}