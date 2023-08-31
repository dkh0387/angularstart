package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class ForgotPasswordException(message: String, httpStatus: HttpStatus) : Throwable() {

}
