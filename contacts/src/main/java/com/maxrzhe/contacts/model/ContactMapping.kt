package com.maxrzhe.contacts.model

import com.maxrzhe.contacts.data.ContactResponseItem
import com.maxrzhe.core.model.Contact

abstract class ContactMapping {
    companion object {
        fun contactToContactRoom(contact: Contact): ContactRoom = with(contact) {
            ContactRoom(
                id = 0,
                fbId = fbId,
                name = name,
                email = email,
                phone = phone,
                image = image,
                birthDate = birthDate,
                isFavorite = isFavorite
            )
        }

        fun contactRoomToContact(contactRoom: ContactRoom): Contact = with(contactRoom) {
            Contact(
                fbId = fbId,
                name = name,
                email = email,
                phone = phone,
                image = image,
                birthDate = birthDate,
                isFavorite = isFavorite
            )
        }


        fun contactRestToContact(
            fbId: String?,
            contactItem: ContactResponseItem,
        ): Contact? = with(contactItem) {
            if (fbId != null) {
                Contact(
                    fbId = fbId,
                    name = name,
                    email = email,
                    image = image,
                    phone = phone,
                    isFavorite = isFavorite,
                    birthDate = birthDate
                )
            } else null
        }

        fun contactToContactRest(contact: Contact): ContactResponseItem = with(contact) {
            ContactResponseItem(name, email, phone, image, birthDate, isFavorite)
        }

        fun contactRoomToContactRest(contactRoom: ContactRoom): ContactResponseItem =
            with(contactRoom) {
                ContactResponseItem(name, email, phone, image, birthDate, isFavorite)
            }

        fun contactRestToContactRoom(
            fbId: String,
            contactItem: ContactResponseItem
        ): ContactRoom = with(contactItem) {
            ContactRoom(
                id = 0,
                fbId = fbId,
                name = name,
                email = email,
                image = image,
                phone = phone,
                isFavorite = isFavorite,
                birthDate = birthDate
            )
        }
    }
}