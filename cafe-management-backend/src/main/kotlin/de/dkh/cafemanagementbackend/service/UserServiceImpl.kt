package de.dkh.cafemanagementbackend.service

import com.fasterxml.jackson.databind.ObjectMapper
import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.exception.*
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtService
import de.dkh.cafemanagementbackend.repository.UserRepository
import de.dkh.cafemanagementbackend.utils.CafeUtils
import de.dkh.cafemanagementbackend.wrapper.UserWrapper
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.util.*

@Service
@Slf4j
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val authenticationManager: AuthenticationManager,
    private val customerUserDetailsService: CustomerUserDetailsService,
    private val jwtService: JwtService,
    private val jwtFilter: JwtFilter
) : UserService {
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
                    CafeUtils.getStringResponseFor(
                        CafeConstants.USER_SUCCESSFULLY_REGISTERED + ": $registeredUser", HttpStatus.CREATED
                    )
                } else {
                    CafeUtils.getStringResponseFor(CafeConstants.EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST)
                }
                // If the incoming request is not valid, return a SignUpErrorResponce
            } else {
                return CafeUtils.getStringResponseFor(
                    SignUpErrorResponce(
                        HttpStatus.BAD_REQUEST.name, CafeConstants.INVALID_DATA, System.currentTimeMillis()
                    ).toString(), HttpStatus.BAD_REQUEST
                )
            }
        } catch (e: Exception) {
            throw SignUpException(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Login implementation.
     * We use [AuthenticationManager] to authenticate a user by the given email.
     */
    override fun logIn(requestMap: Map<String, String>): ResponseEntity<String> {
        println("Inside logIn $requestMap")

        try {
            // simple presentation of a username and password
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    requestMap["email"], requestMap["password"]
                )
            )

            if (authentication.isAuthenticated) {
                // Check if the user is activated and return the token: <token> response
                return if (customerUserDetailsService.checkUserApproved()) {
                    val tokenKeyWord = "token"
                    val token = jwtService.generateToken(
                        customerUserDetailsService.getUserDetailWithoutPassword().email,
                        customerUserDetailsService.getUserDetailWithoutPassword().role
                    )
                    CafeUtils.getStringResponseFor("{\"$tokenKeyWord\":\"$token\"}", HttpStatus.OK)
                } else {
                    val messageKeyWord = "message"
                    CafeUtils.getStringResponseFor(
                        "{\"$messageKeyWord\":\"${CafeConstants.WAIT_FOR_ADMIN_APPROVAL}\"}",
                        HttpStatus.BAD_REQUEST
                    )
                }
            } else {
                return CafeUtils.getStringResponseFor(CafeConstants.BAD_CREDENTIALS, HttpStatus.INTERNAL_SERVER_ERROR)
            }

        } catch (e: Exception) {
            throw SignUpException(CafeConstants.LOGIN_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun getAllUsers(): ResponseEntity<List<UserWrapper>> {
        println("Inside getAllUsers")

        try {
            return if (jwtFilter.isAdmin()) {
                ResponseEntity<List<UserWrapper>>(
                    userRepository.findAll().filter { it.role.equals("user", true) }.map { it.toWrapper() },
                    HttpStatus.OK
                )
            } else {
                CafeUtils.getUsersResponseFor(emptyList(), HttpStatus.UNAUTHORIZED)
            }
        } catch (e: Exception) {
            throw UsersLoadException(CafeConstants.LOAD_USERS_WENT_WRONG, HttpStatus.BAD_REQUEST)
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