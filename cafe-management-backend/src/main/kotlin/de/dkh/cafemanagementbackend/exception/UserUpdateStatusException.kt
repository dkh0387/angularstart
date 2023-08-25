package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class UserUpdateStatusException(override val message: String, val httpStatus: HttpStatus) : Throwable() {

}
