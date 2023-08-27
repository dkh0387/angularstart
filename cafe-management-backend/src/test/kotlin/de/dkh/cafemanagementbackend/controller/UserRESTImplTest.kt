package de.dkh.cafemanagementbackend.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtService
import de.dkh.cafemanagementbackend.repository.UserRepository
import de.dkh.cafemanagementbackend.testutils.TestData
import jakarta.servlet.ServletException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.nio.charset.StandardCharsets


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
//@ActiveProfiles(value = ["test"])
class UserRESTImplTest {

    private val BASE_URL = "http://localhost:8081/user"

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var jwtService: JwtService

    @Autowired
    lateinit var jwtFilter: JwtFilter

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

    @Nested
    @DisplayName("Testing web layer for user log in")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext
    inner class LogInTesting {

        @Test
        fun `should return 'INTERNAL_SERVER_ERROR' response if an exception occurs`() {

            // given
            val user = TestData.getInactiveUser().copy(email = "dfhlfwhewio")

            // when / then
            val resultActionsDsl = (mockMvc.post("$BASE_URL/login") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(user)
            })

            resultActionsDsl
                .andDo { print() }
                .andExpect {
                    status { isInternalServerError() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                }

        }

        @Test
        fun `should return 'OK' response if no exceptions occur`() {

            // given
            val user = TestData.getInactiveUser().copy(email = "deniskh87@gmail.com")

            // when / then
            val resultActionsDsl = (mockMvc.post("$BASE_URL/login") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(user)
            })

            resultActionsDsl
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
                }
        }
    }

    @Nested
    @DisplayName("Testing web layer for loading all users as wrappers")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext
    inner class LoadAllUsersTesting {

        @Test
        fun `should return a response with all users as wrappers`() {
            // given
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            // when
            val resultActionsDsl = mockMvc.get("$BASE_URL/get")

            // then
            assertNotNull(token)
            resultActionsDsl
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].name") { value("David Adams") }
                }
        }

    }

    @Nested
    @DisplayName("Testing web layer for updating user status by admin")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext
    inner class UpdateUserStatusTesting {

        @Test
        fun `should return a NO_USER_FOR_ID response if there is no id in the request map`() {

            // given
            val user = TestData.getInactiveUser().copy(status = "false")

            // when
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = mockMvc.post("$BASE_URL/update") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(user)
            }

            // then
            val mvcResult = resultActionsDsl
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
                }
                .andReturn()
            assertThat(mvcResult.response.contentAsString).isEqualTo(CafeConstants.NO_USER_FOR_ID)

        }

        @Test
        fun `should update user status if there is a valid id in the request map`() {

            // given
            val statusJson = "{\n" +
                    "  \"id\": \"1\",\n" +
                    "  \"status\": \"true\"\n" +
                    "}"

            // when
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = (mockMvc.post("$BASE_URL/update") {
                contentType = MediaType.APPLICATION_JSON
                content = statusJson
            })

            // then
            resultActionsDsl
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
                }
        }

    }
}