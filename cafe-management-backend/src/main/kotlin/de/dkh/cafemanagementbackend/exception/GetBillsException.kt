package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class GetBillsException(message: String, httpStatus: HttpStatus) : Throwable() {

}
