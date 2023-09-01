package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class UpdateCategoryException(message: String, httpStatus: HttpStatus) : Throwable() {

}
