package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class UpdateProductException(message: String, httpStatus: HttpStatus) : Throwable() {

}
