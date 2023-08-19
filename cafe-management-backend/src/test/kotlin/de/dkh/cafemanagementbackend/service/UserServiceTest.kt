package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.exception.SignUpException
import de.dkh.cafemanagementbackend.exception.SignUpValidationException
import de.dkh.cafemanagementbackend.repository.UserRepository
import de.dkh.cafemanagementbackend.testutils.TestData
import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


@ExtendWith(MockKExtension::class)
class UserServiceTest {

    private val userRepository = mockk<UserRepository>()
    private val objectUnderTest: UserServiceImpl = UserServiceImpl(userRepository)

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
        fun `should return the 'EMAIL_ALREADY_EXISTS' response if the requested user is already registered`() {
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
}