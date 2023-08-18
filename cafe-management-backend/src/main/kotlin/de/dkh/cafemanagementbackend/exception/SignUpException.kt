package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

data class SignUpException(override val message: String, val httpStatus: HttpStatus) : Exception()
