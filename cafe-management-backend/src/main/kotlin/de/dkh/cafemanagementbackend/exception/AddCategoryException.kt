package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus

class AddCategoryException(addCategoryWentWrong: String, httpStatus: HttpStatus) : Throwable() {

}
