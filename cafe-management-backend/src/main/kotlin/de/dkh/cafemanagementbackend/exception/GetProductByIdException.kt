package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class GetProductByIdException(message: String, httpStatus: HttpStatus) : Throwable() {

}
