package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtService
import de.dkh.cafemanagementbackend.repository.CategoryRepository
import de.dkh.cafemanagementbackend.repository.UserRepository
import de.dkh.cafemanagementbackend.testutils.TestData
import io.mockk.clearAllMocks
import jakarta.servlet.ServletException
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.nio.charset.StandardCharsets

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CategoryRESTImplTest {

    private val BASE_URL = "http://localhost:8081/category"

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Autowired
    lateinit var jwtService: JwtService

    @Autowired
    lateinit var jwtFilter: JwtFilter

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        val admin = userRepository.findById(6)
        admin.get().password = TestData.getAdmin().password
        jwtFilter.setCurrentUser(null)
    }

    @Nested
    @DisplayName("Testing web layer for adding category")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext
    inner class AddCategoryTesting {

        @Test
        fun `should throw an exception if the provided map contains wrong fields`() {
            // given
            val name = "dsfsfdsf"
            val categoryJson = "{\n" +
                    "  \"name2\": \"${name}\" \n" +
                    "}"
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            // when/then
            assertThatThrownBy {
                (mockMvc.post("$BASE_URL/add") {
                    contentType = MediaType.APPLICATION_JSON
                    content = categoryJson
                })
            }.isInstanceOf(ServletException::class.java)
        }

        @Test
        fun `should return an UNAUTHORIZED_ACCESS response if current user is not an admin`() {
            // given
            val name = "dsfsfdsf"
            val categoryJson = "{\n" +
                    "  \"name\": \"${name}\" \n" +
                    "}"
            val token = jwtService.generateToken("david@luv2code.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            // when
            val resultActionsDsl = (mockMvc.post("$BASE_URL/add") {
                contentType = MediaType.APPLICATION_JSON
                content = categoryJson
            })

            // then
            val mvcResult = resultActionsDsl
                .andDo { print() }
                .andExpect {
                    status { isUnauthorized() }
                    content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
                }
                .andReturn()

            Assertions.assertThat(mvcResult.response.contentAsString).isEqualTo(CafeConstants.UNAUTHORIZED_ACCESS)
        }

        @Test
        fun `should return an ADD_CATEGORY_SUCCESSFULLY response and save category if current user is an admin`() {
            // given
            val name = "Testcategory"

            categoryRepository.deleteByName(name)

            val categoryJson = "{\n" +
                    "  \"name\": \"${name}\" \n" +
                    "}"
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            // when
            val resultActionsDsl = (mockMvc.post("$BASE_URL/add") {
                contentType = MediaType.APPLICATION_JSON
                content = categoryJson
            })

            // then
            val mvcResult = resultActionsDsl
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
                }
                .andReturn()

            Assertions.assertThat(mvcResult.response.contentAsString).isEqualTo(CafeConstants.ADD_CATEGORY_SUCCESSFULLY)

            categoryRepository.deleteByName(name)
        }

    }
}