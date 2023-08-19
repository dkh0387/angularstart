package de.dkh.cafemanagementbackend.testutils

import de.dkh.cafemanagementbackend.entity.User

class TestData {

    companion object {

        fun getInactiveUser(): User = User(
            name = "Denis Khaskin",
            contactNumber = "+4915126227287",
            email = "deniskh87@gmail.com",
            password = "11235813",
            status = "false",
            role = "user"
        )
    }
}