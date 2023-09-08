package de.dkh.cafemanagementbackend.utils

import de.dkh.cafemanagementbackend.wrapper.CategoryWrapper
import de.dkh.cafemanagementbackend.wrapper.ProductWrapper
import de.dkh.cafemanagementbackend.wrapper.UserWrapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CafeUtils {

    companion object {

        fun getStringResponseFor(body: String, httpStatus: HttpStatus): ResponseEntity<String> =
            ResponseEntity(body, httpStatus)

        fun getUsersResponseFor(body: List<UserWrapper>, httpStatus: HttpStatus): ResponseEntity<List<UserWrapper>> =
            ResponseEntity(body, httpStatus)

        fun getCategoryResponseFor(
            body: List<CategoryWrapper>,
            httpStatus: HttpStatus
        ): ResponseEntity<List<CategoryWrapper>> =
            ResponseEntity(body, httpStatus)

        fun getProductResponseFor(
            body: List<ProductWrapper>,
            httpStatus: HttpStatus
        ): ResponseEntity<List<ProductWrapper>> =
            ResponseEntity(body, httpStatus)


    }
}