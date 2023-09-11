package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class DeleteProductException(message: String, httpStatus: HttpStatus) : Throwable() {

}
