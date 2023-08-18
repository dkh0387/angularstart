package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.exception.SignUpException
import de.dkh.cafemanagementbackend.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserRESTImpl(private val userService: UserService) : UserREST {
    override fun signUp(requestMap: Map<String, String>): ResponseEntity<String> {
        try {
            return userService.signUp(requestMap)
        } catch (e: Exception) {
            throw SignUpException(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}