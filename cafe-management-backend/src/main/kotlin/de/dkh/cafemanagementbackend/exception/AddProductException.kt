package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class AddProductException(message: String, val httpStatus: HttpStatus) : Throwable() {

}
