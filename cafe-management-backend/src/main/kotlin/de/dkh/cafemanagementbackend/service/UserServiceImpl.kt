package de.dkh.cafemanagementbackend.service

import com.fasterxml.jackson.databind.ObjectMapper
import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.exception.SignUpValidationException
import lombok.extern.slf4j.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Slf4j
class UserServiceImpl : UserService {
    override fun signUp(requestMap: Map<String, String>): ResponseEntity<String> {
        TODO("Not yet implemented")
    }

    fun validateSignUpMap(requestMap: Map<String, String>): User {
        val objectMapper = ObjectMapper()
        try {
            val json =
                objectMapper.writer().withoutAttribute("status").withoutAttribute("role").writeValueAsString(requestMap)
            return objectMapper.readValue(json, User::class.java)
        } catch (e: Exception) {
            throw SignUpValidationException("Map $requestMap could not be parsed as User object!")
        }

    }
}