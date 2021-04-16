package com.maxrzhe.contactsapp.model

abstract class ContactMapping {
    companion object {
        fun contactToContactRoom(contact: Contact): ContactRoom = with(contact) {
            ContactRoom(id = id, name = name, email = email, phone = phone, image = image)
        }

        fun contactRoomToContact(contactRoom: ContactRoom): Contact = with(contactRoom) {
            Contact.Existing(id = id, name = name, email = email, phone = phone, image = image)
        }

        fun contactRoomToContact(contactRoomList: List<ContactRoom>?): List<Contact>? =
            contactRoomList?.map { contactRoomToContact(it) }
    }
}