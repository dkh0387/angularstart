package de.dkh.cafemanagementbackend.service

import com.fasterxml.jackson.databind.ObjectMapper
import de.dkh.cafemanagementbackend.entity.User
import lombok.extern.slf4j.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Slf4j
class UserServiceImpl : UserService {
    override fun signUp(requestMap: Map<String, String>): ResponseEntity<String> {
        TODO("Not yet implemented")
    }

    private fun validateSignUpMap(requestMap: Map<String, String>) {

        val objectMapper = ObjectMapper().readerForMapOf(User::class.java)

    }
}