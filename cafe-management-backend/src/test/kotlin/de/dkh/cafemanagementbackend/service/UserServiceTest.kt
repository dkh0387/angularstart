package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.exception.SignUpValidationException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

class UserServiceTest {

    private val objectUnderTest: UserServiceImpl = UserServiceImpl()

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
                    name = requestMap.get("name")!!,
                    contactNumber = requestMap.get("contactNumber")!!,
                    email = requestMap.get("email")!!,
                    password = requestMap.get("password")!!,
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