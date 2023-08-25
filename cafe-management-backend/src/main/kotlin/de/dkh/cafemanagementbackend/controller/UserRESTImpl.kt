package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.exception.LogInException
import de.dkh.cafemanagementbackend.exception.SignUpException
import de.dkh.cafemanagementbackend.exception.UsersLoadException
import de.dkh.cafemanagementbackend.service.UserService
import de.dkh.cafemanagementbackend.wrapper.UserWrapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserRESTImpl(private val userService: UserService) : UserREST {
    @Throws(SignUpException::class)
    override fun signUp(requestMap: Map<String, String>): ResponseEntity<String> {
        return userService.signUp(requestMap)
    }

    @Throws(LogInException::class)
    override fun logIn(requestMap: Map<String, String>): ResponseEntity<String> {
        return userService.logIn(requestMap)
    }

    @Throws(UsersLoadException::class)
    override fun getAllUsers(): ResponseEntity<List<UserWrapper>> {
        return userService.getAllUsers()
    }


}