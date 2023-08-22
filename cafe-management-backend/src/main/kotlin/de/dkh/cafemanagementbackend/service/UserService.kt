package de.dkh.cafemanagementbackend.service

import org.springframework.http.ResponseEntity

interface UserService {
    fun signUp(requestMap: Map<String, String>): ResponseEntity<String>

    fun logIn(requestMap: Map<String, String>): ResponseEntity<String>

}