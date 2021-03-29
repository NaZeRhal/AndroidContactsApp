package com.maxrzhe.contactsapp.data

import com.maxrzhe.contactsapp.model.Contact

class DummyData {

    companion object {
        fun load(): ArrayList<Contact> {
            return arrayListOf(
                Contact(
                    id = 1,
                    name = "Mark Smith",
                    phone = "+555-003-6324545",
                    email = "mark.smith@gmail.com",
                    image = "android.resource://com.maxrzhe.contactsapp/drawable/person_one"
                ),
                Contact(
                    id = 2,
                    name = "John Dow",
                    phone = "+555-432-6544165",
                    email = "john.dow@gmail.com",
                    image = "android.resource://com.maxrzhe.contactsapp/drawable/person_two"
                ),
                Contact(
                    id = 3,
                    name = "Karla Look",
                    phone = "+555-203-4322172",
                    email = "karla.look@gmail.com",
                    image = "android.resource://com.maxrzhe.contactsapp/drawable/person_three"
                ),
                Contact(
                    id = 4,
                    name = "Mark Johns",
                    phone = "+555-112-3103498",
                    email = "mark.johns@gmail.com",
                    image = "android.resource://com.maxrzhe.contactsapp/drawable/person_four"
                ),
                Contact(
                    id = 5,
                    name = "Paula Smith",
                    phone = "+555-112-6324449",
                    email = "paula.smith@gmail.com",
                    image = "android.resource://com.maxrzhe.contactsapp/drawable/person_five"
                ),

                Contact(
                    id = 6,
                    name = "Raulia Gonsalez",
                    phone = "+555-234-9878162",
                    email = "raulia.gonsalez@gmail.com",
                    image = "android.resource://com.maxrzhe.contactsapp/drawable/person_six"
                ),
                Contact(
                    id = 7,
                    name = "Ivan Pirogov",
                    phone = "+375-29-3455645",
                    email = "mark.smith@gmail.com",
                    image = "android.resource://com.maxrzhe.contactsapp/drawable/person_seven"
                ),
                Contact(
                    id = 8,
                    name = "Ilia Dorogin",
                    phone = "+375-29-3455645",
                    email = "ilia.dorogin@gmail.com",
                    image = "android.resource://com.maxrzhe.contactsapp/drawable/person_eight"
                ),
                Contact(
                    id = 9,
                    name = "Maksim Maksimov",
                    phone = "+375-29-5487856",
                    email = "maksim.maksimov@gmail.com",
                    image = "android.resource://com.maxrzhe.contactsapp/drawable/person_two"
                ),
                Contact(
                    id = 10,
                    name = "Larisa Maksimov",
                    phone = "+375-29-4684745",
                    email = "larisa.maksimova@gmail.com",
                    image = "android.resource://com.maxrzhe.contactsapp/drawable/person_three"
                ),
                Contact(
                    id = 11,
                    name = "Gogi Gogenov",
                    phone = "+305-297-5698741",
                    email = "gogi.gogenov@gmail.com",
                    image = "android.resource://com.maxrzhe.contactsapp/drawable/person_seven"
                ),
                Contact(
                    id = 12,
                    name = "Samanta Sorov",
                    phone = "+555-303-547865",
                    email = "samanta.sorov@gmail.com",
                    image = "android.resource://com.maxrzhe.contactsapp/drawable/person_five"
                ),
                Contact(
                    id = 13,
                    name = "Lilia Gromova",
                    phone = "+370-89-2569874",
                    email = "lilia.gromova@gmail.com",
                    image = "android.resource://com.maxrzhe.contactsapp/drawable/person_one"
                )
            )
        }
    }
}