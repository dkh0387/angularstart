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

    fun validateSignUpMap(requestMap: Map<String, String>): Boolean {
        val objectMapper = ObjectMapper()
        try {
            val jsonInString = "{\"name\":\"mkyong\",\"age\":37,\"skills\":[\"java\",\"python\"]}"
            val user2 = objectMapper.readValue(jsonInString, User::class.java)




            val json = objectMapper.writeValueAsString(requestMap)
            val user = objectMapper.readValue(json, User::class.java)
            return user is User
        } catch (e: Exception) {
            throw SignUpValidationException("Map $requestMap could not be parsed as User object!")
        }

    }
}