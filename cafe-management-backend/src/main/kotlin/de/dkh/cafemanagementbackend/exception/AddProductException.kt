package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class AddProductException(message: String, httpStatus: HttpStatus) : Throwable() {

}
