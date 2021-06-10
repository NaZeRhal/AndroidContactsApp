package com.maxrzhe.domain.usecases

import com.example.data_api.model.Contact
import com.example.data_api.repositories.ContactRepository
import com.maxrzhe.common.util.Constants
import com.maxrzhe.common.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddContactAfterPushNotificationUseCase(private val repository: ContactRepository) :
    UseCaseWithParams<Map<String, String>, Flow<Resource<Contact>>>() {
    override suspend fun buildUseCase(data: Map<String, String>): Flow<Resource<Contact>> {
        val fbId = data[Constants.KEY_FB_ID] ?: ""
        val name = data[Constants.KEY_NAME]
        val email = data[Constants.KEY_EMAIL]
        val phone = data[Constants.KEY_PHONE]
        val image = data[Constants.KEY_IMAGE] ?: ""
        val birthDate = data[Constants.KEY_BIRTH_DATE]
        val isFavorite = data[Constants.KEY_IS_FAVORITE].toBoolean()

        return if (!name.isNullOrEmpty() && !email.isNullOrEmpty() && !phone.isNullOrEmpty() && !birthDate.isNullOrEmpty()) {
            val contact = Contact(
                fbId = fbId,
                name = name,
                email = email,
                phone = phone,
                image = image,
                birthDate = birthDate,
                isFavorite = isFavorite
            )
            repository.add(contact)
        } else flow { emit(Resource.Error<Contact>(Throwable("Failed adding contact with invalid fields"))) }
    }
}