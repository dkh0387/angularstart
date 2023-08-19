package de.dkh.cafemanagementbackend.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.exception.InvalidEmailException
import de.dkh.cafemanagementbackend.exception.SignUpException
import de.dkh.cafemanagementbackend.exception.SignUpValidationException
import de.dkh.cafemanagementbackend.repository.UserRepository
import de.dkh.cafemanagementbackend.service.UserService
import de.dkh.cafemanagementbackend.testutils.TestData
import io.mockk.every
import io.mockk.mockk
import jakarta.servlet.ServletException
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.nio.charset.StandardCharsets

@SpringBootTest
@AutoConfigureMockMvc
class UserRESTImplTest {

    private val BASE_URL = "http://localhost:8081/user"

    @Autowired
    lateinit var mockMvc: MockMvc

    /**
     * ObjectMapper from jackson to convert JSON-POJO and reversed.
     */
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var userRepository: UserRepository

    @Nested
    @DisplayName("Testing web layer for user sign up")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext
    inner class SavingUser {
        @Test
        fun `should save a new user in the database if there is no user with the given email`() {

            userRepository.deleteByEmail("testforsave@gmail.com")

            val numberOFUsersBeforePOST = userRepository.findAll().count()

            val resultActionsDsl = mockMvc.post("$BASE_URL/signup") {
                contentType = MediaType.APPLICATION_JSON
                val newUser = TestData.getInactiveUser().copy(email = "testforsave@gmail.com")
                content = objectMapper.writeValueAsString(newUser)
            }

            resultActionsDsl
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
                }

            val numberOFUsersAfterPOST = userRepository.findAll().count()

            assert(numberOFUsersAfterPOST == numberOFUsersBeforePOST + 1)

            userRepository.deleteByEmail("testforsave@gmail.com")
        }

        @Test
        fun `should return 'EMAIL_ALREADY_EXISTS' response if there is a user with the given email`() {

            val numberOFUsersBeforePOST = userRepository.findAll().count()

            val resultActionsDsl = mockMvc.post("$BASE_URL/signup") {
                contentType = MediaType.APPLICATION_JSON
                val newUser = TestData.getInactiveUser()
                content = objectMapper.writeValueAsString(newUser)
            }

            resultActionsDsl
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                    content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
                }

            val numberOFUsersAfterPOST = userRepository.findAll().count()

            assert(numberOFUsersAfterPOST == numberOFUsersBeforePOST)

        }

        @Test
        fun `should return 'INTERNAL_SERVER_ERROR' response if an exception occurs`() {

            assertThatThrownBy {
                (mockMvc.post("$BASE_URL/signup") {
                    contentType = MediaType.APPLICATION_JSON
                    val newUser = TestData.getInactiveUser().copy(email = "fjgroltkoksd")
                    content = objectMapper.writeValueAsString(newUser)
                })
            }.isInstanceOf(ServletException::class.java)
        }

    }
}