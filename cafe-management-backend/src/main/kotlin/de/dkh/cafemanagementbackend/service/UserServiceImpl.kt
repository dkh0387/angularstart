package de.dkh.cafemanagementbackend.service

import com.fasterxml.jackson.databind.ObjectMapper
import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.exception.InvalidEmailException
import de.dkh.cafemanagementbackend.exception.SignUpErrorResponce
import de.dkh.cafemanagementbackend.exception.SignUpException
import de.dkh.cafemanagementbackend.exception.SignUpValidationException
import de.dkh.cafemanagementbackend.repository.UserRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.security.InvalidParameterException
import java.util.*

@Service
@Slf4j
class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override fun signUp(requestMap: Map<String, String>): ResponseEntity<String> {
        println("Inside signUp $requestMap")

        try {
            // Validate the incoming request and map it to a User instance
            val userFromMap = validateSignUpMap(requestMap)

            // If the mapped user is valid proof if he/she is already registered
            if (userFromMap != null) {
                val existingUser = userRepository.findByEmail(userFromMap.email)

                return if (Objects.isNull(existingUser)) {
                    val registeredUser = register(userFromMap)
                    ResponseEntity<String>(
                        CafeConstants.USER_SUCCESSFULLY_REGISTERED + ": $registeredUser",
                        HttpStatus.CREATED
                    )
                } else {
                    ResponseEntity<String>(CafeConstants.EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST)
                }
                // If the incoming request is not valid, return a SignUpErrorResponce
            } else {
                return ResponseEntity<String>(
                    SignUpErrorResponce(
                        HttpStatus.BAD_REQUEST.name,
                        CafeConstants.INVALID_DATA,
                        System.currentTimeMillis()
                    ).toString(), HttpStatus.BAD_REQUEST
                )
            }
        } catch (e: Exception) {
            throw SignUpException(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
        }
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

    private fun register(user: User): User {
        user.status = "false"
        user.role = "user"
        return if (isValidEmail(user.email)) userRepository.save(user) else throw InvalidEmailException(
            CafeConstants.INVALID_EMAIL
        )
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return email.matches(emailRegex.toRegex())
    }

}