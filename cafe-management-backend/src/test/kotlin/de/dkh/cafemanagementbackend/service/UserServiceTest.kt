package de.dkh.cafemanagementbackend.service

import org.assertj.core.api.Assertions.assertThat
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
        fun `should return true if the map contains a valid User object`() {
            // given
            val requestMap: Map<String, String> =
                mapOf(
                    "name" to "Denis Khaskin",
                    "contactNumber" to "+4915126227287",
                    "email" to "deniskh87@gmail.com",
                    "password" to "11235813"
                )


            // when
            val result = objectUnderTest.validateSignUpMap(requestMap)

            // then
            assertThat(result).isTrue()

        }

    }
}