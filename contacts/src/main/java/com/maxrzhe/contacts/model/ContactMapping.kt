package com.maxrzhe.contacts.model

import com.maxrzhe.contacts.data.ContactListResponseItem
import com.maxrzhe.core.model.Contact

abstract class ContactMapping {
    companion object {
        fun contactToContactRoom(contact: Contact): ContactRoom = with(contact) {
            if (this is Contact.Existing) {
                ContactRoom(
                    id = id,
                    name = name,
                    email = email,
                    phone = phone,
                    image = image,
                    birthDate = birthDate,
                    isFavorite = isFavorite
                )
            } else {
                ContactRoom(
                    name = name,
                    email = email,
                    phone = phone,
                    image = image,
                    birthDate = birthDate,
                    isFavorite = isFavorite
                )
            }
        }

        fun contactRoomToContact(contactRoom: ContactRoom?): Contact.Existing? = with(contactRoom) {
            if (this != null) {
                Contact.Existing(
                    id = id,
                    name = name,
                    email = email,
                    phone = phone,
                    image = image,
                    birthDate = birthDate,
                    isFavorite = isFavorite
                )
            } else null
        }

        fun contactRoomToContact(contactRoomList: List<ContactRoom>?): List<Contact.Existing>? =
            contactRoomList?.mapNotNull {
                contactRoomToContact(it)
            }

        fun contactRestToContact(contactItem: ContactListResponseItem?): Contact.Existing? =
            with(contactItem) {
                if (this != null) {
                    Contact.Existing(
                        id = id,
                        name = name,
                        email = email,
                        phone = phone,
                        image = image,
                        birthDate = birthDate,
                        isFavorite = isFavorite
                    )
                } else null
            }

        fun contactRestToContact(contactItemList: List<ContactListResponseItem>?): List<Contact.Existing>? =
            contactItemList?.mapNotNull {
                contactRestToContact(it)
            }
    }
}