package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.exception.SignUpValidationException
import de.dkh.cafemanagementbackend.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class UserServiceTest(@Mock val userRepository: UserRepository) {

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
}