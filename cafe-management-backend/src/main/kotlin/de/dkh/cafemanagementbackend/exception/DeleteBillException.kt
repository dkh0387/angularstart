package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class DeleteBillException(message: String, httpStatus: HttpStatus) : Throwable() {

}
