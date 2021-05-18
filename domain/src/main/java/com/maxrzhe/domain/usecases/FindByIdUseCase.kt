package com.maxrzhe.domain.usecases

import com.example.data_api.model.Contact
import com.example.data_api.repositories.ContactRepository
import com.maxrzhe.common.util.Resource
import kotlinx.coroutines.flow.Flow

class FindByIdUseCase(private val contactRepository: ContactRepository) :
    UseCaseWithParams<String, Flow<Resource<Contact>>>() {
    override suspend fun buildUseCase(fbId: String): Flow<Resource<Contact>> {
        return contactRepository.findById(fbId)
    }
}