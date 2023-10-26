package de.dkh.cafemanagementbackend.service

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.exception.*
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtService
import de.dkh.cafemanagementbackend.repository.UserRepository
import de.dkh.cafemanagementbackend.testutils.TestData
import de.dkh.cafemanagementbackend.utils.CafeUtils
import de.dkh.cafemanagementbackend.utils.EmailUtils
import de.dkh.cafemanagementbackend.wrapper.UserWrapper
import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import java.util.*


@ExtendWith(MockKExtension::class)
class UserServiceTest {

    private val userRepository = mockk<UserRepository>()
    private val authenticationManager = mockk<AuthenticationManager>()
    private val customerUserDetailsService = mockk<CustomerUserDetailsService>()
    private val jwtService = mockk<JwtService>()
    private val jwtFilter = mockk<JwtFilter>()
    private val emailUtils = mockk<EmailUtils>()
    private val objectUnderTest: UserServiceImpl =
        UserServiceImpl(
            userRepository,
            authenticationManager,
            customerUserDetailsService,
            jwtService,
            jwtFilter,
            emailUtils
        )

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Nested
    @DisplayName("Testing SignUp Map Validator")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class SignUpMapValidatorTesting {

        @Test
        fun `should return a valid user if the map contains valid User properties`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "name" to "Denis Khaskin",
                "contactNumber" to "+4915126227287",
                "email" to "deniskh87@gmail.com",
                "password" to "11235813"
            )


            // when
            val result = objectUnderTest.validateSignUpMap(requestMap, false)

            // then
            assertThat(result.toString()).isEqualTo(
                User(
                    name = requestMap["name"]!!,
                    contactNumber = requestMap["contactNumber"]!!,
                    email = requestMap["email"]!!,
                    password = requestMap["password"]!!,
                    status = User.DEFAULT_STATUS,
                    role = User.DEFAULT_ROLE,
                    authorities = emptyList()
                ).toString()
            )

        }

        @Test
        fun `should throw an exception if the map contains unknown User properties`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "name2" to "Denis Khaskin",
                "contactNumber2" to "+4915126227287",
                "email" to "deniskh87@gmail.com",
                "password" to "11235813"
            )

            // when/then
            assertThatThrownBy { objectUnderTest.validateSignUpMap(requestMap, false) }.isInstanceOf(
                SignUpValidationException::class.java
            )
        }

    }

    @Nested
    @DisplayName("Testing sign up")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class SignUpTesting {

        @Test
        fun `should throw a SignUpValidationException if the request map is corrupt`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "name2" to "Denis Khaskin",
                "contactNumber2" to "+4915126227287",
                "email" to "deniskh87@gmail.com",
                "password" to "11235813"
            )

            // when/then
            assertThatThrownBy { objectUnderTest.signUp(requestMap) }.isInstanceOf(SignUpValidationException::class.java)
        }

        @Test
        fun `should throw a SignUpException if there is no email provided in the map`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "name" to "Denis Khaskin",
                "contactNumber" to "+4915126227287",
                "email" to "null",
                "password" to "11235813"
            )

            // when/then
            assertThatThrownBy { objectUnderTest.signUp(requestMap) }.isInstanceOf(SignUpException::class.java)
        }

        @Test
        fun `should return the EMAIL_ALREADY_EXISTS response if the requested user is already registered`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "name" to "Denis Khaskin",
                "contactNumber" to "+4915126227287",
                "email" to "deniskh87@gmail.com",
                "password" to "11235813"
            )

            // when
            every { userRepository.findByEmail(any()) } returns TestData.getInactiveUser()
            val responseEntity = objectUnderTest.signUp(requestMap)

            // then
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.EMAIL_ALREADY_EXISTS,
                    HttpStatus.BAD_REQUEST
                )
            )
        }

        @Test
        fun `should save a new user if the requested user is not registered yet`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "name" to "Denis Khaskin",
                "contactNumber" to "+4915126227287",
                "email" to "deniskh87@gmail.com",
                "password" to "11235813"
            )

            // when
            every { userRepository.findByEmail(any()) } returns null
            every { userRepository.save(any()) } returns TestData.getInactiveUser()
            objectUnderTest.signUp(requestMap)

            // then
            verify(exactly = 1) { userRepository.save(any()) }
        }

    }

    @Nested
    @DisplayName("Testing Log in")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class LogInTesting {

        @Test
        fun `should throw a SignUpException when the authentication fails`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "email" to "deniskh87@gmail.com",
                "password" to "11235813"
            )

            every { authenticationManager.authenticate(any()) } throws RuntimeException("Authentication failed!")

            // when / then
            assertThatThrownBy { objectUnderTest.logIn(requestMap) }.isInstanceOf(SignUpException::class.java)

        }

        @Test
        fun `should return a BAD_REQUEST response when user is not authenticated`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "email" to "deniskh87@gmail.com",
                "password" to "11235813"
            )

            every { authenticationManager.authenticate(any()).isAuthenticated } returns false
            every { customerUserDetailsService.loadUserByUsername(any()) } returns TestData.getInactiveUser()
                .toUserDetails()


            // when
            val response = objectUnderTest.logIn(requestMap)

            // then
            assertThat(response).isEqualTo(
                CafeUtils.getStringResponseFor(
                    CafeConstants.BAD_CREDENTIALS,
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
        }

        @Test
        fun `should return a BAD_REQUEST response when user is not approved (status = false)`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "email" to "deniskh87@gmail.com",
                "password" to "11235813"
            )

            every { authenticationManager.authenticate(any()).isAuthenticated } returns true
            every { customerUserDetailsService.checkUserApproved() } returns false
            every { customerUserDetailsService.loadUserByUsername(any()) } returns TestData.getInactiveUser()
                .toUserDetails()

            // when
            val messageKeyWord = "message"
            val response = objectUnderTest.logIn(requestMap)

            // then
            assertThat(response).isEqualTo(
                CafeUtils.getStringResponseFor(
                    "{\"$messageKeyWord\":\"${CafeConstants.WAIT_FOR_ADMIN_APPROVAL}\"}",
                    HttpStatus.BAD_REQUEST
                )
            )
        }

        @Test
        fun `should return a OK response with generated JWT when user is approved (status = true)`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "email" to "deniskh87@gmail.com",
                "password" to "11235813"
            )

            val tokenKeyWord = "token"

            every { authenticationManager.authenticate(any()).isAuthenticated } returns true
            every { customerUserDetailsService.checkUserApproved() } returns true
            every { customerUserDetailsService.getUserDetailWithoutPassword() } returns TestData.getUserDetailWithoutPassword()
            every { jwtService.generateToken(any(), any()) } returns tokenKeyWord
            every { customerUserDetailsService.loadUserByUsername(any()) } returns TestData.getInactiveUser()
                .toUserDetails()


            // when
            val response = objectUnderTest.logIn(requestMap)

            // then
            assertThat(response).isEqualTo(
                CafeUtils.getStringResponseFor("{\"$tokenKeyWord\":\"$tokenKeyWord\"}", HttpStatus.OK)
            )
        }

    }

    @Nested
    @DisplayName("Testing update user status")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class UpdateUserStatusTesting {

        @Test
        fun `should throw a UsersLoadException if the repository throws an exception`() {
            // given
            every { userRepository.findAll() } throws Exception()

            // when / then
            assertThatThrownBy { objectUnderTest.getAllUsers() }.isInstanceOf(UsersLoadException::class.java)
        }

        @Test
        fun `should return a BAD_REQUEST response if the user is not an admin`() {
            // given
            every { jwtFilter.isAdmin() } returns false

            // when / then
            val responseEntity = objectUnderTest.getAllUsers()

            // then
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<List<UserWrapper>>(
                    emptyList(), HttpStatus.UNAUTHORIZED
                )
            )
        }

        @Test
        fun `should return a valid UserWrapper response if the repository loads all users`() {
            // given
            every { jwtFilter.isAdmin() } returns true
            every { userRepository.findAll() } returns listOf(TestData.getInactiveUserWithAuthorities())

            // when / then
            val responseEntity = objectUnderTest.getAllUsers()

            // then
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<List<UserWrapper>>(
                    listOf(
                        TestData.getInactiveUser().toWrapper()
                    ), HttpStatus.OK
                )
            )
        }

    }

    @Nested
    @DisplayName("Testing get all users as wrappers")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAllUsersTesting {

        @Test
        fun `should throw a UserUpdateStatusException if the repository throws an exception`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "name" to "Denis Khaskin",
                "contactNumber" to "+4915126227287",
                "email" to "deniskh87@gmail.com",
                "password" to "11235813",
                "status" to "false"
            )

            every { jwtFilter.isAdmin() } returns true
            every { userRepository.findById(any()) } throws Exception()

            // when / then
            assertThatThrownBy { objectUnderTest.update(requestMap) }.isInstanceOf(UserUpdateStatusException::class.java)
        }

        @Test
        fun `should return a UNAUTHORIZED_ACCESS response if the user is not an admin`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "name" to "Denis Khaskin",
                "contactNumber" to "+4915126227287",
                "email" to "deniskh87@gmail.com",
                "password" to "11235813",
                "status" to "false"
            )

            every { jwtFilter.isAdmin() } returns false

            // when / then
            val responseEntity = objectUnderTest.update(requestMap)

            // then
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED
                )
            )
        }

        @Test
        fun `should return a NO_USER_FOR_ID response if there is no user for the given id in the database`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "id" to "10",
                "name" to "Denis Khaskin",
                "contactNumber" to "+4915126227287",
                "email" to "deniskh87@gmail.com",
                "password" to "11235813",
                "status" to "false"
            )

            every { jwtFilter.isAdmin() } returns true
            every { userRepository.findById(any()) } returns Optional.empty()

            // when / then
            val responseEntity = objectUnderTest.update(requestMap)

            // then
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.NO_USER_FOR_ID, HttpStatus.OK
                )
            )
        }

        @Test
        fun `should update the user status and return a valid response if there is a user for the given id in the database`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "id" to "1",
                "status" to "true"
            )

            every { jwtFilter.isAdmin() } returns true
            every { jwtFilter.getCurrentUser() } returns null
            every { userRepository.findById(any()) } returns Optional.of(TestData.getInactiveUser())
            every { userRepository.updateStatus(any(), any()) } returns 1
            every { userRepository.getAllAdmins("admin") } returns emptyList()

            // when
            val responseEntity = objectUnderTest.update(requestMap)

            // then
            verify(exactly = 1) { userRepository.updateStatus(any(), any()) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.USER_STATUS_UPDATED, HttpStatus.OK
                )
            )
        }

        @Test
        fun `should not update the user status and return a NO_STATUS_REQUESTED_FOR_UPDATE response if there is no status in the request map`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "id" to "1"
            )

            every { jwtFilter.isAdmin() } returns true
            every { jwtFilter.getCurrentUser() } returns TestData.getSpringUserDetails()
            every { userRepository.findById(any()) } returns Optional.of(TestData.getInactiveUser())
            every { userRepository.updateStatus(any(), any()) } returns 1
            every {
                userRepository.getAllAdmins(
                    User.UserRoles.ROLE_ADMIN.nameWithoutPrefix().lowercase()
                )
            } returns emptyList()
            every { emailUtils.sendSimpleMessage(any(), any(), any(), any()) } just runs

            // when
            val responseEntity = objectUnderTest.update(requestMap)

            // then
            verify(exactly = 0) { userRepository.updateStatus(any(), any()) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.NO_STATUS_REQUESTED_FOR_UPDATE, HttpStatus.OK
                )
            )
        }
    }

    @Nested
    @DisplayName("Testing send email about user status update")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class SendEmailTesting {

        @Test
        fun `should log a WARN message if the current user is null`() {
            // given
            every { jwtFilter.getCurrentUser() } returns null

            val logger: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java) as Logger
            val listAppender = ListAppender<ILoggingEvent>()
            listAppender.start()
            logger.addAppender(listAppender)
            val logsList = listAppender.list

            // when
            objectUnderTest.sendEmailToAllAdmin("true", "deniskh87@gmail.com", emptyList())

            // then
            assertEquals(CafeConstants.CURRENT_USER_IS_NULL, logsList[0].formattedMessage)
            assertEquals(Level.WARN, logsList[0].level)

            verify(exactly = 0) { emailUtils.sendSimpleMessage(any(), any(), any(), any()) }

        }

        @Test
        fun `should log a WARN message if no status provided for update`() {
            // given
            every { jwtFilter.getCurrentUser() } returns TestData.getSpringUserDetails()

            val logger: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java) as Logger
            val listAppender = ListAppender<ILoggingEvent>()
            listAppender.start()
            logger.addAppender(listAppender)
            val logsList = listAppender.list

            // when
            objectUnderTest.sendEmailToAllAdmin(null, "deniskh87@gmail.com", emptyList())

            // then
            assertEquals(CafeConstants.NO_STATUS_FOR_UPDATE, logsList[0].formattedMessage)
            assertEquals(Level.WARN, logsList[0].level)

            verify(exactly = 0) { emailUtils.sendSimpleMessage(any(), any(), any(), any()) }
        }

        @Test
        fun `should send an email if current user is not null and status provided for update`() {
            // given
            every { jwtFilter.getCurrentUser() } returns TestData.getSpringUserDetails()
            every { emailUtils.sendSimpleMessage(any(), any(), any(), any()) } just runs

            // when
            objectUnderTest.sendEmailToAllAdmin("true", "deniskh87@gmail.com", emptyList())

            // then
            verify(exactly = 1) { emailUtils.sendSimpleMessage(any(), any(), any(), any()) }
        }
    }

    @Nested
    @DisplayName("Testing change passwort")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class ChangePasswordTesting {

        @Test
        fun `should throw a ChangePasswordException if a corrupt request map is provided`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "oldPassword2" to "12345",
                "newPassword" to "123456"
            )

            every { jwtFilter.getCurrentUser() } returns TestData.getSpringUserDetails()
            every { userRepository.findByEmail(any()) } returns TestData.getInactiveUser()

            // when / then
            assertThatThrownBy { objectUnderTest.changePassword(requestMap) }.isInstanceOf(ChangePasswordException::class.java)
        }

        @Test
        fun `should return  a NO_USER_FOR_EMAIL response if a correct request map is provided`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "oldPassword" to "12345",
                "newPassword" to "123456"
            )

            every { jwtFilter.getCurrentUser() } returns TestData.getSpringUserDetails()
            every { userRepository.findByEmail(any()) } returns null

            // when
            val responseEntity = objectUnderTest.changePassword(requestMap)

            // then
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.NO_USER_FOR_EMAIL,
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
        }

        @Test
        fun `should return a OLD_VS_EXISTING_PASSWORD_MISMATCH response if oldPasswort from request map and existing user password do not match`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "oldPassword" to "12345",
                "newPassword" to "123456"
            )

            every { jwtFilter.getCurrentUser() } returns TestData.getSpringUserDetails()
            every { userRepository.findByEmail(any()) } returns TestData.getInactiveUser().copy(password = "fdkjafpoej")

            // when
            val responseEntity = objectUnderTest.changePassword(requestMap)

            // then
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.OLD_VS_EXISTING_PASSWORD_MISMATCH,
                    HttpStatus.BAD_REQUEST
                )
            )
        }

        @Test
        fun `should save a new password and return a PASSWORD_SUCCESSFULLY_CHANGED response if oldPasswort from request map and existing user password match`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "oldPassword" to "12345",
                "newPassword" to "123456"
            )

            every { jwtFilter.getCurrentUser() } returns TestData.getSpringUserDetails()
            every { userRepository.findByEmail(any()) } returns TestData.getInactiveUser().copy(password = "12345")
            every {
                userRepository.save(
                    userRepository.save(
                        TestData.getInactiveUser().copy(password = "123456")
                    )
                )
            } returns TestData.getInactiveUser().copy(password = "12345")

            // when
            val responseEntity = objectUnderTest.changePassword(requestMap)

            // then
            verify(exactly = 1) { userRepository.save(TestData.getInactiveUser().copy(password = "123456")) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeUtils.formatBodyAsJSON(CafeConstants.PASSWORD_SUCCESSFULLY_CHANGED),
                    HttpStatus.OK
                )
            )
        }

    }

    @Nested
    @DisplayName("Testing forgot passwort")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class ForgotPasswordTesting {

        @Test
        fun `should throw an exception if the provided map contains wrong fields`() {
            // given
            val requestMap = mapOf(
                "email2" to "dkhfsaö@test.com"
            )
            every { userRepository.findByEmail("dkhfsaö@test.com") } returns null

            // when / then
            assertThatThrownBy { objectUnderTest.forgotPassword(requestMap) }.isInstanceOf(ForgotPasswordException::class.java)
        }

        @Test
        fun `should return a NO_USER_FOR_EMAIL response if the provided email does not exist`() {
            // given
            val requestMap = mapOf(
                "email" to "dkhfsaö@test.com"
            )
            every { userRepository.findByEmail("dkhfsaö@test.com") } returns null

            // when
            val responseEntity = objectUnderTest.forgotPassword(requestMap)

            // then
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.NO_USER_FOR_EMAIL,
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
        }

        @Test
        fun `should return a FORGOT_PASSWORD_SUCCESSFULLY response and reset user password if the provided request map is ok`() {
            // given
            val requestMap = mapOf(
                "email" to "dkhfsaö@test.com"
            )
            every { userRepository.findByEmail("dkhfsaö@test.com") } returns TestData.getInactiveUser()
            every { userRepository.save(any()) } returns TestData.getInactiveUser()
            every {
                emailUtils.forgotEmail(
                    TestData.getInactiveUser().email,
                    CafeConstants.FORGOT_PASSWORD_SUBJECT,
                    any()
                )
            } just runs

            // when
            val responseEntity = objectUnderTest.forgotPassword(requestMap)

            // then
            verify(exactly = 1) {
                emailUtils.forgotEmail(
                    TestData.getInactiveUser().email,
                    CafeConstants.FORGOT_PASSWORD_SUBJECT,
                    any()
                )
            }

            verify(exactly = 1) { userRepository.save(any()) }

            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeUtils.formatBodyAsJSON(CafeConstants.FORGOT_PASSWORD_SUCCESSFULLY),
                    HttpStatus.OK
                )
            )
        }

    }
}