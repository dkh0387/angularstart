package de.dkh.cafemanagementbackend.service

import com.fasterxml.jackson.databind.ObjectMapper
import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.Authority
import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.exception.*
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtService
import de.dkh.cafemanagementbackend.repository.UserRepository
import de.dkh.cafemanagementbackend.utils.CafeUtils
import de.dkh.cafemanagementbackend.utils.EmailUtils
import de.dkh.cafemanagementbackend.wrapper.UserWrapper
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.MailAuthenticationException
import org.springframework.mail.MailException
import org.springframework.mail.MailParseException
import org.springframework.mail.MailSendException
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
    private val jwtFilter: JwtFilter,
    private val emailUtils: EmailUtils
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
            throw SignUpException(
                CafeConstants.SOMETHING_WENT_WRONG + " MESSAGE: " + e.localizedMessage,
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    /**
     * Login implementation.
     * We use [AuthenticationManager] to authenticate a user by the given email.
     */
    override fun logIn(requestMap: Map<String, String>): ResponseEntity<String> {
        println("Inside logIn $requestMap")

        try {
            // in order to set the userDetails for customerUserDetailsService
            customerUserDetailsService.loadUserByUsername(requestMap["email"])
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
                        customerUserDetailsService.getUserDetailWithoutPassword().authorities!!
                            .sortedWith(authorityComparator).first().authority
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
            throw SignUpException(
                CafeConstants.LOGIN_WENT_WRONG + " MESSAGE: " + e.localizedMessage,
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    override fun
            getAllUsers(): ResponseEntity<List<UserWrapper>> {
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
            throw UsersLoadException(
                CafeConstants.LOAD_USERS_WENT_WRONG + " MESSAGE: " + e.localizedMessage,
                HttpStatus.BAD_REQUEST
            )
        }
    }

    /**
     * Implementation of updating a user status by admin.
     * User for update is within the request map.
     *
     */
    override fun update(requestMap: Map<String, String>): ResponseEntity<String> {
        println("Inside update $requestMap")

        try {
            return if (jwtFilter.isAdmin()) {
                val userOptional = userRepository.findById(Integer.parseInt(requestMap["id"]).toLong())
                // if the user exists update the status
                if (userOptional.isEmpty) {
                    return CafeUtils.getStringResponseFor(CafeConstants.NO_USER_FOR_ID, HttpStatus.OK)
                } else {
                    if (requestMap["status"] != null) {
                        requestMap["status"]?.let { userRepository.updateStatus(userOptional.get().id, it) }
                        sendEmailToAllAdmin(
                            requestMap["status"],
                            userOptional.get().email,
                            userRepository.getAllAdmins("admin")
                        )
                        return CafeUtils.getStringResponseFor(
                            CafeConstants.USER_STATUS_UPDATED,
                            HttpStatus.OK
                        )
                    } else {
                        return CafeUtils.getStringResponseFor(
                            CafeConstants.NO_STATUS_REQUESTED_FOR_UPDATE,
                            HttpStatus.OK
                        )
                    }

                }
            } else {
                CafeUtils.getStringResponseFor(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED)
            }

        } catch (e: Exception) {
            throw UserUpdateStatusException(
                CafeConstants.USER_STATUS_UPDATE_WENT_WRONG + " MESSAGE: " + e.localizedMessage,
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    /**
     * @TODO: testing!
     * Send an email to all admins about status update for the requested user.
     * NOTE: the username of UserDetails is being used as email in this project, see: [de.dkh.cafemanagementbackend.service.CustomerUserDetailsService].
     */
    @Throws(
        MailException::class,
        MailAuthenticationException::class,
        MailParseException::class,
        MailSendException::class
    )
    override fun sendEmailToAllAdmin(status: String?, email: String, allAdmins: List<UserWrapper>) {

        if (jwtFilter.getCurrentUser() != null && status != null) {
            if (status.equals("true", true)) {
                emailUtils.sendSimpleMessage(
                    to = jwtFilter.getCurrentUser()!!.username,
                    subject = CafeConstants.SUBJECT_USER_SET_APPROVED,
                    text = CafeConstants.TEXT_USER_SET_APPROVED + " USER: $email" + " ADMIN: ${jwtFilter.getCurrentUser()}",
                    allAdmins.map { it.email }
                )
            } else if (status.equals("false", true)) {
                emailUtils.sendSimpleMessage(
                    to = jwtFilter.getCurrentUser()!!.username,
                    subject = CafeConstants.SUBJECT_USER_SET_DISABLED,
                    text = CafeConstants.TEXT_USER_SET_DISABLED + " USER: $email" + " ADMIN: ${jwtFilter.getCurrentUser()}",
                    allAdmins.map { it.email }
                )
            }
        }
    }

    /**
     * We are using this method as a flag to check, whether the provided JWT corresponds to the admin role.
     * When a user goes through the app and access admin resources, the method returns 403, otherwise OK.
     */
    override fun checkToken(): ResponseEntity<String> {
        return CafeUtils.getStringResponseFor("true", HttpStatus.OK)
    }

    /**
     * @TODO: testing!
     * Changing password for a user.
     * NOTE: [de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter.currentUser] must be set here!
     */
    override fun changePassword(requestMap: Map<String, String>): ResponseEntity<String> {
        try {
            val user = userRepository.findByEmail(jwtFilter.getCurrentUser()!!.username)
                ?: return CafeUtils.getStringResponseFor(
                    CafeConstants.NO_USER_FOR_EMAIL,
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            // Check if the old password in the request map and the existing one match...
            val oldVSExistingPassword = checkOldVSExistingPassword(requestMap["oldPassword"], user.password)

            // If so, update
            return if (!oldVSExistingPassword) {
                CafeUtils.getStringResponseFor(
                    CafeConstants.OLD_VS_EXISTING_PASSWORD_MISMATCH,
                    HttpStatus.BAD_REQUEST
                )
            } else {
                user.password = requestMap["newPassword"]
                userRepository.save(user)
                CafeUtils.getStringResponseFor(CafeConstants.PASSWORD_SUCCESSFULLY_CHANGED, HttpStatus.OK)
            }

        } catch (e: Exception) {
            throw ChangePasswordException(
                CafeConstants.CHANGE_PASSWORD_WENT_WRONG + " MESSAGE: " + e.localizedMessage,
                HttpStatus.INTERNAL_SERVER_ERROR
            )
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

    private fun checkOldVSExistingPassword(oldPassword: String?, existingPassword: String?): Boolean {
        if (oldPassword == null || existingPassword == null) {
            return false
        }
        return oldPassword == existingPassword
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

    private val authorityComparator = Comparator<Authority> { a1, a2 ->
        when {
            (a1 == null && a2 == null) -> 0
            (a1 == null) -> -1
            else -> User.UserRoles.valueOf(a1.authority).compareTo(User.UserRoles.valueOf(a2.authority))
        }
    }

}