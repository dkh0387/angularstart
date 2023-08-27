package de.dkh.cafemanagementbackend.testutils

import de.dkh.cafemanagementbackend.entity.Authority
import de.dkh.cafemanagementbackend.entity.User

class TestData {

    companion object {

        fun getInactiveUser(): User = User(
            name = "Denis Khaskin",
            contactNumber = "+4915126227287",
            email = "deniskh87@gmail.com",
            password = "11235813",
            status = "false",
            role = User.UserRoles.ROLE_USER.nameWithoutPrefix()
        )

        fun getAuthority(): Authority = Authority(User.UserRoles.ROLE_USER.name)

        fun getUserDetailWithoutPassword(): User =
            getInactiveUserWithAuthorities().copy(password = null, status = "true")

        fun getSpringUserDetails(): org.springframework.security.core.userdetails.User =
            org.springframework.security.core.userdetails.User(
                getInactiveUser().email,
                getInactiveUser().password,
                ArrayList()
            )

        fun getInactiveUserWithAuthorities(): User = getInactiveUser().copy(authorities = listOf(getAuthority()))
    }
}