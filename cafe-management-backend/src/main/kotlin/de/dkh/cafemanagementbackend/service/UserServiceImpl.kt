package de.dkh.cafemanagementbackend.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {
    override fun signUp(requestMap: Map<String, String>): ResponseEntity<String> {
        TODO("Not yet implemented")
    }
}