package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class AddCategoryException(message: String, val httpStatus: HttpStatus) : Throwable()
