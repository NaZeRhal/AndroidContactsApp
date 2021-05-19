package com.maxrzhe.domain.usecases

import com.example.data_api.model.Contact
import com.example.data_api.repositories.ContactRepository

class DeleteContactUseCase(private val contactRepository: ContactRepository) :
    UseCaseWithParams<Contact, Unit>() {
    override suspend fun buildUseCase(contact: Contact) {
        contactRepository.delete(contact)
    }

}