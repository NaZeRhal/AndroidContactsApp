package com.maxrzhe.contacts.model

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
                    birthDate = birthDate
                )
            } else {
                ContactRoom(
                    name = name,
                    email = email,
                    phone = phone,
                    image = image,
                    birthDate = birthDate
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
                    birthDate = birthDate
                )
            } else null
        }

        fun contactRoomToContact(contactRoomList: List<ContactRoom>?): List<Contact.Existing>? =
            contactRoomList?.map {
                with(it) {
                    Contact.Existing(
                        id = id,
                        name = name,
                        email = email,
                        phone = phone,
                        image = image,
                        birthDate = birthDate
                    )
                }
            }
    }
}