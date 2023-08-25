package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.exception.SignUpException
import de.dkh.cafemanagementbackend.exception.SignUpValidationException
import de.dkh.cafemanagementbackend.exception.UserUpdateStatusException
import de.dkh.cafemanagementbackend.exception.UsersLoadException
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtService
import de.dkh.cafemanagementbackend.repository.UserRepository
import de.dkh.cafemanagementbackend.testutils.TestData
import de.dkh.cafemanagementbackend.utils.CafeUtils
import de.dkh.cafemanagementbackend.wrapper.UserWrapper
import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import java.lang.Exception
import java.lang.RuntimeException
import java.util.*


@ExtendWith(MockKExtension::class)
class UserServiceTest {

    private val userRepository = mockk<UserRepository>()
    private val authenticationManager = mockk<AuthenticationManager>()
    private val customerUserDetailsService = mockk<CustomerUserDetailsService>()
    private val jwtService = mockk<JwtService>()
    private val jwtFilter = mockk<JwtFilter>()
    private val objectUnderTest: UserServiceImpl =
        UserServiceImpl(userRepository, authenticationManager, customerUserDetailsService, jwtService, jwtFilter)

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
            val result = objectUnderTest.validateSignUpMap(requestMap)

            // then
            assertThat(result.toString()).isEqualTo(
                User(
                    name = requestMap["name"]!!,
                    contactNumber = requestMap["contactNumber"]!!,
                    email = requestMap["email"]!!,
                    password = requestMap["password"]!!,
                    status = "null",
                    role = "null"
                ).toString()
            )

        }

        @Test
        fun `should return a valid user if the map does not contains valid User properties`() {
            // given
            val requestMap: Map<String, String> = mapOf(
                "name2" to "Denis Khaskin",
                "contactNumber2" to "+4915126227287",
                "email" to "deniskh87@gmail.com",
                "password" to "11235813"
            )

            // when/then
            assertThatThrownBy { objectUnderTest.validateSignUpMap(requestMap) }.isInstanceOf(SignUpValidationException::class.java)
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
            every { userRepository.findAll() } returns listOf(TestData.getInactiveUser())

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
                "status" to "user"
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
                "status" to "user"
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
                "status" to "user"
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
                "status" to "admin"
            )

            every { jwtFilter.isAdmin() } returns true
            every { userRepository.findById(any()) } returns Optional.of(TestData.getInactiveUser())
            every { userRepository.updateStatus(any(), any()) } returns TestData.getInactiveUser()

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
            every { userRepository.findById(any()) } returns Optional.of(TestData.getInactiveUser())
            every { userRepository.updateStatus(any(), any()) } returns TestData.getInactiveUser()

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
}