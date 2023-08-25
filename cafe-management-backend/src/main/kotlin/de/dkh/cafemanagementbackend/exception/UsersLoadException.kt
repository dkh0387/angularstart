package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class UsersLoadException(override val message: String, val httpStatus: HttpStatus) : Throwable() {

}
