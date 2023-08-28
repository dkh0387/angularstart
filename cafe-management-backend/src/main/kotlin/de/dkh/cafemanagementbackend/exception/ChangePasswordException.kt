package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class ChangePasswordException(message: String, httpStatus: HttpStatus) : Throwable() {

}
