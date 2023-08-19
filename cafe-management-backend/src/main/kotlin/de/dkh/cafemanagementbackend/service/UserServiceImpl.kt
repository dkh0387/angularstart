package de.dkh.cafemanagementbackend.service

import com.fasterxml.jackson.databind.ObjectMapper
import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.exception.SignUpErrorResponce
import de.dkh.cafemanagementbackend.exception.SignUpValidationException
import de.dkh.cafemanagementbackend.repository.UserRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Slf4j
class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override fun signUp(requestMap: Map<String, String>): ResponseEntity<String> {
        println("Inside signUp $requestMap")

        if (validateSignUpMap(requestMap) != null) {
            val user = requestMap["email"]?.let { userRepository.findByEmail(it) }
        } else {
            return ResponseEntity<String>(
                SignUpErrorResponce(
                    HttpStatus.BAD_REQUEST.name,
                    CafeConstants.INVALID_DATA,
                    System.currentTimeMillis()
                ).toString(), HttpStatus.BAD_REQUEST
            )
        }
        return ResponseEntity<String>("TEST", HttpStatus.ACCEPTED)
    }

    fun validateSignUpMap(requestMap: Map<String, String>): User? {
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