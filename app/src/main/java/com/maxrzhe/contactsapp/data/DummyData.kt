package com.maxrzhe.contactsapp.data

import com.maxrzhe.contactsapp.model.Contact


class DummyData {

    companion object {
        fun load(): ArrayList<Contact> {
            val contacts = ArrayList<Contact>()

            return contacts.apply {
                add(
                    Contact(
                        id = 1,
                        name = "Mark Smith",
                        phone = "+555-003-6324545",
                        email = "mark.smith@gmail.com",
                        image = "person_one"
                    )
                )
                add(
                    Contact(
                        id = 2,
                        name = "John Dow",
                        phone = "+555-432-6544165",
                        email = "john.dow@gmail.com",
                        image = "person_two"
                    )
                )
                add(
                    Contact(
                        id = 3,
                        name = "Karla Look",
                        phone = "+555-203-4322172",
                        email = "karla.look@gmail.com",
                        image = "person_three"
                    )
                )
                add(
                    Contact(
                        id = 4,
                        name = "Mark Johns",
                        phone = "+555-112-3103498",
                        email = "mark.johns@gmail.com",
                        image = "person_four"
                    )
                )
                add(
                    Contact(
                        id = 5,
                        name = "Paula Smith",
                        phone = "+555-112-6324449",
                        email = "paula.smith@gmail.com",
                        image = "person_five"
                    )
                )
                add(
                    Contact(
                        id = 6,
                        name = "Raulia Gonsalez",
                        phone = "+555-234-9878162",
                        email = "raulia.gonsalez@gmail.com",
                        image = "person_six"
                    )
                )
                add(
                    Contact(
                        id = 7,
                        name = "Ivan Pirogov",
                        phone = "+375-29-3455645",
                        email = "mark.smith@gmail.com",
                        image = "person_seven"
                    )
                )
                add(
                    Contact(
                        id = 8,
                        name = "Ilia Dorogin",
                        phone = "+375-29-3455645",
                        email = "ilia.dorogin@gmail.com",
                        image = "person_eight"
                    )
                )
                add(
                    Contact(
                        id = 9,
                        name = "Maksim Maksimov",
                        phone = "+375-29-5487856",
                        email = "maksim.maksimov@gmail.com",
                        image = "person_two"
                    )
                )
                add(
                    Contact(
                        id = 10,
                        name = "Larisa Maksimov",
                        phone = "+375-29-4684745",
                        email = "larisa.maksimova@gmail.com",
                        image = "person_three"
                    )
                )
                add(
                    Contact(
                        id = 11,
                        name = "Gogi Gogenov",
                        phone = "+305-297-5698741",
                        email = "gogi.gogenov@gmail.com",
                        image = "person_seven"
                    )
                )

                add(
                    Contact(
                        id = 12,
                        name = "Samanta Sorov",
                        phone = "+555-303-547865",
                        email = "samanta.sorov@gmail.com",
                        image = "person_five"
                    )
                )
                add(
                    Contact(
                        id = 13,
                        name = "Lilia Gromova",
                        phone = "+370-89-2569874",
                        email = "lilia.gromova@gmail.com",
                        image = "person_five"
                    )
                )
            }
        }
    }

}