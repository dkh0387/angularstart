package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class GenerateBillException(message: String, httpStatus: HttpStatus) : Throwable() {

}
