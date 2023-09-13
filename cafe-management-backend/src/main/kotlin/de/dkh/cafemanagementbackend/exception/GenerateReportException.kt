package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class GenerateReportException(message: String, httpStatus: HttpStatus) : Throwable() {

}
