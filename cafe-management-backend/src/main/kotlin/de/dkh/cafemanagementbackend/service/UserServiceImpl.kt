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
import de.dkh.cafemanagementbackend.utils.mapper.*
import de.dkh.cafemanagementbackend.wrapper.UserWrapper
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
    private val emailUtils: EmailUtils,
    private val objectMapper: ObjectMapper
) : UserService {

    private val logger: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    override fun signUp(requestMap: Map<String, String>): ResponseEntity<String> {
        println("Inside signUp $requestMap")

        try {
            // Validate the incoming request and map it to a User instance
            val userFromMap = validateSignUpMap(requestMap)

            // If the mapped user is valid proof if he/she is already registered
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
            val userMapperSimple = getMapperFromRequestMap(requestMap, UserMapperSimple::class.java) as UserMapperSimple
            val userFromMap = User.createFromSimple(userMapperSimple)
            // in order to set the userDetails for customerUserDetailsService
            customerUserDetailsService.loadUserByUsername(userFromMap.email)
            // simple presentation of a username and password
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    userFromMap.email, userFromMap.password
                )
            )

            if (authentication.isAuthenticated) {
                // Check if the user is activated and return the token: <token> response
                return if (customerUserDetailsService.checkUserApproved()) {
                    val token = jwtService.generateToken(
                        customerUserDetailsService.getUserDetailWithoutPassword().email,
                        customerUserDetailsService.getUserDetailWithoutPassword().authorities!!
                            .sortedWith(authorityComparator).first().authority
                    )
                    CafeUtils.getStringResponseFor("{\"${CafeConstants.TOKEN_KEY_WORD}\":\"$token\"}", HttpStatus.OK)
                } else {
                    CafeUtils.getStringResponseFor(
                        "{\"${CafeConstants.MESSAGE_KEY_WORD}\":\"${CafeConstants.WAIT_FOR_ADMIN_APPROVAL}\"}",
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
                    userRepository.findAll().filter { it.role.equals(User.DEFAULT_ROLE, true) }.map { it.toWrapper() },
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
            val userMapperFull = getMapperFromRequestMap(requestMap, UserMapperFull::class.java) as UserMapperFull

            if (userMapperFull.status == "") {
                return CafeUtils.getStringResponseFor(
                    CafeConstants.NO_STATUS_REQUESTED_FOR_UPDATE,
                    HttpStatus.OK
                )
            }
            val userFromMap = User.createFromFull(userMapperFull)
            return if (jwtFilter.isAdmin()) {
                val userOptional = userRepository.findById(Integer.parseInt(userFromMap.id.toString()).toLong())
                // if the user exists, update the status
                if (userOptional.isEmpty) {
                    return CafeUtils.getStringResponseFor(CafeConstants.NO_USER_FOR_ID, HttpStatus.OK)
                } else {
                    run {
                        userRepository.updateStatus(userOptional.get().id, userFromMap.status)
                        sendEmailToAllAdmin(
                            userFromMap.status,
                            userOptional.get().email,
                            userRepository.getAllAdmins(
                                User.UserRoles.ROLE_ADMIN.nameWithoutPrefix()
                                    .lowercase(Locale.getDefault())
                            )
                        )
                        return CafeUtils.getStringResponseFor(
                            CafeConstants.USER_STATUS_UPDATED,
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
     * Sends Email to all admins about status update for the requested user.
     * NOTE: the username of UserDetails is being used as email in this project, see: [de.dkh.cafemanagementbackend.service.CustomerUserDetailsService].
     */
    @Throws(
        MailException::class,
        MailAuthenticationException::class,
        MailParseException::class,
        MailSendException::class
    )
    override fun sendEmailToAllAdmin(
        status: String?,
        email: String,
        allAdmins: List<UserWrapper>
    ) {

        if (jwtFilter.getCurrentUser() != null && status != null) {
            if (status.equals(CafeConstants.TRUE, true)) {
                emailUtils.sendSimpleMessage(
                    to = jwtFilter.getCurrentUser()!!.username,
                    subject = CafeConstants.SUBJECT_USER_SET_APPROVED,
                    text = CafeConstants.TEXT_USER_SET_APPROVED + " USER: $email" + " ADMIN: ${jwtFilter.getCurrentUser()}",
                    allAdmins.map { it.email }
                )
            } else if (status.equals(User.DEFAULT_STATUS, true)) {
                emailUtils.sendSimpleMessage(
                    to = jwtFilter.getCurrentUser()!!.username,
                    subject = CafeConstants.SUBJECT_USER_SET_DISABLED,
                    text = CafeConstants.TEXT_USER_SET_DISABLED + " USER: $email" + " ADMIN: ${jwtFilter.getCurrentUser()}",
                    allAdmins.map { it.email }
                )
            }
        } else {
            logger.warn(if (jwtFilter.getCurrentUser() == null) CafeConstants.CURRENT_USER_IS_NULL else CafeConstants.NO_STATUS_FOR_UPDATE)
        }
    }

    /**
     * We are using this method as a flag to check, whether the provided JWT corresponds to the admin role.
     * When a user goes through the app and access admin resources, the method returns 403, otherwise OK.
     */
    override fun checkToken(): ResponseEntity<String> {
        return CafeUtils.getStringResponseFor(CafeConstants.TRUE, HttpStatus.OK)
    }

    /**
     * Changing password for a user.
     * NOTE: [de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter.currentUser] must be set here!
     */
    override fun changePassword(requestMap: Map<String, String>): ResponseEntity<String> {
        try {
            val passwordMapper =
                getMapperFromRequestMap(requestMap, ChangePasswordMapper::class.java) as ChangePasswordMapper
            val user = userRepository.findByEmail(jwtFilter.getCurrentUser()!!.username)
                ?: return CafeUtils.getStringResponseFor(
                    CafeConstants.NO_USER_FOR_EMAIL,
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            // Check if the old password in the request map and the existing one match...
            val oldVSExistingPassword = checkOldVSExistingPassword(passwordMapper.oldPassword, user.password)

            // If so, update
            return if (!oldVSExistingPassword) {
                CafeUtils.getStringResponseFor(
                    CafeConstants.OLD_VS_EXISTING_PASSWORD_MISMATCH,
                    HttpStatus.BAD_REQUEST
                )
            } else {
                user.password = passwordMapper.newPassword
                userRepository.save(user)
                return CafeUtils.getStringResponseFor(CafeConstants.PASSWORD_SUCCESSFULLY_CHANGED, HttpStatus.OK)
            }

        } catch (e: Exception) {
            throw ChangePasswordException(
                CafeConstants.CHANGE_PASSWORD_WENT_WRONG + " MESSAGE: " + e.localizedMessage,
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    /**
     * @TODO: testing!
     * Send an email if the user click on "forgot password".
     * The method set a temporary password, which should be reset by user.
     */
    override fun forgotPassword(requestMap: Map<String, String>): ResponseEntity<String> {
        try {
            // first get the current user
            val forgotPasswortMapper =
                getMapperFromRequestMap(requestMap, ForgotPasswordMapper::class.java) as ForgotPasswordMapper
            val user = userRepository.findByEmail(forgotPasswortMapper.email)
                ?: return CafeUtils.getStringResponseFor(
                    CafeConstants.NO_USER_FOR_EMAIL,
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            // if the user is there and have a valid email, send an email with a random password
            val randomPassword = generateRandomPassword()
            emailUtils.forgotEmail(
                user.email,
                CafeConstants.FORGOT_PASSWORD_SUBJECT,
                randomPassword
            )
            user.password = randomPassword
            userRepository.save(user)
            return ResponseEntity(CafeConstants.FORGOT_PASSWORD_SUCCESSFULLY, HttpStatus.OK)

        } catch (e: Exception) {
            throw ForgotPasswordException(CafeConstants.FORGOT_PASSWORD_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


    private fun generateRandomPassword(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..CafeConstants.RANDOM_PASSWORD_LENGTH)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun validateSignUpMap(requestMap: Map<String, String>): User {
        try {
            val userMapperSimple = getMapperFromRequestMap(requestMap, UserMapperSimple::class.java) as UserMapperSimple
            return User.createFromSimple(userMapperSimple)
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
        user.status = User.DEFAULT_STATUS
        user.role = User.DEFAULT_ROLE
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

    /**
     * Maps a given request map to the corresponding mapper class.
     */
    private fun getMapperFromRequestMap(requestMap: Map<String, String>, clazz: Class<out KeyMapper>): KeyMapper {
        val json = objectMapper.writer().writeValueAsString(requestMap)
        return objectMapper.readValue(json, clazz)
    }
}