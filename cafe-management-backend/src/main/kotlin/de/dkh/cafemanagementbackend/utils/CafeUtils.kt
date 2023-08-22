package de.dkh.cafemanagementbackend.utils

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CafeUtils {

    companion object {
        fun getResponseFor(body: String, httpStatus: HttpStatus): ResponseEntity<String> =
            ResponseEntity(body, httpStatus)
    }
}