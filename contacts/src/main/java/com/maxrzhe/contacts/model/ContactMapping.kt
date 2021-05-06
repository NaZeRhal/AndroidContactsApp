package com.maxrzhe.contacts.model

import com.maxrzhe.contacts.data.ContactListResponseItem
import com.maxrzhe.core.model.Contact

abstract class ContactMapping {
    companion object {
        fun contactToContactRoom(contact: Contact): ContactRoom = with(contact) {
            if (this is Contact.Existing) {
                ContactRoom(
                    id = id,
                    fbId = fbId,
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
                    fbId = fbId,
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
                    fbId = fbId,
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

        fun contactRestToContactExisting(
            fbId: String,
            contactItem: ContactListResponseItem,
            contact: Contact.Existing
        ): Contact.Existing = with(contactItem) {
            Contact.Existing(
                id = contact.id,
                fbId = fbId,
                name = name,
                email = email,
                image = image,
                phone = phone,
                isFavorite = isFavorite,
                birthDate = birthDate
            )
        }

        fun contactRestToContactNew(
            fbId:String,
            contactItem: ContactListResponseItem,
        ): Contact.New = with(contactItem) {
            Contact.New(
                fbId = fbId,
                name = name,
                email = email,
                image = image,
                phone = phone,
                isFavorite = isFavorite,
                birthDate = birthDate
            )
        }

        fun contactRestToContact(
            fbId: String,
            contactItem: ContactListResponseItem?
        ): Contact.New? =
            with(contactItem) {
                if (this != null) {
                    Contact.New(
                        fbId = fbId,
                        name = name,
                        email = email,
                        phone = phone,
                        image = image,
                        birthDate = birthDate,
                        isFavorite = isFavorite
                    )
                } else null
            }

        fun contactRestToContact(contactItemList: HashMap<String, ContactListResponseItem>?): List<Contact.New>? =
            contactItemList?.mapNotNull {
                contactRestToContact(it.key, it.value)
            }
    }
}