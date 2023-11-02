package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class DeleteCategoryException(message: String, httpStatus: HttpStatus) : Throwable() {

}

