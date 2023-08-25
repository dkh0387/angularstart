package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.wrapper.UserWrapper
import org.springframework.http.ResponseEntity

interface UserService {
    fun signUp(requestMap: Map<String, String>): ResponseEntity<String>

    fun logIn(requestMap: Map<String, String>): ResponseEntity<String>
    fun getAllUsers(): ResponseEntity<List<UserWrapper>>
    fun update(requestMap: Map<String, String>): ResponseEntity<String>

    fun sendEmailToAllAdmin(status: String?, email: String, allAdmins: List<UserWrapper>)

}