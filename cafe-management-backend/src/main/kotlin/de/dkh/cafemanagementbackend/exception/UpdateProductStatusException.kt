package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class UpdateProductStatusException(message: String, httpStatus: HttpStatus) : Throwable() {

}
