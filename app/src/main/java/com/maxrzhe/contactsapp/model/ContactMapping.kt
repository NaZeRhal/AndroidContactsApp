package com.maxrzhe.contactsapp.model

abstract class ContactMapping {
    companion object {
        fun contactToContactRoom(contact: Contact): ContactRoom = with(contact) {
            ContactRoom(id, name, phone, email, image)
        }

        fun contactRoomToContact(contactRoom: ContactRoom): Contact = with(contactRoom) {
            Contact(id, name, phone, email, image)
        }

        fun contactRoomToContact(contactRoomList: List<ContactRoom>?): List<Contact>? =
            contactRoomList?.map { contactRoomToContact(it) }
    }
}