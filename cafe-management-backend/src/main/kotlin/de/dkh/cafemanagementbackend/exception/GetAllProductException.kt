package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class GetAllProductException(message: String, httpStatus: HttpStatus) : Throwable() {

}
