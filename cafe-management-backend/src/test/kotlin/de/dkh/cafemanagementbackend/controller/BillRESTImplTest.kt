package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtService
import de.dkh.cafemanagementbackend.repository.BillRepository
import de.dkh.cafemanagementbackend.testutils.TestData
import de.dkh.cafemanagementbackend.utils.CafeUtils
import io.mockk.clearAllMocks
import jakarta.servlet.ServletException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.*
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
class BillRESTImplTest {

    private val BASE_URL = "http://localhost:8081/bill"

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var billRepository: BillRepository

    @Autowired
    lateinit var jwtService: JwtService

    @Autowired
    lateinit var jwtFilter: JwtFilter

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        jwtFilter.setCurrentUser(null)
    }

    @Nested
    @DisplayName("Testing web layer for generating bills")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext
    inner class GenerateBillTesting {
        @Test
        fun `should throw an exception while trying to generate a new bill with a corrupt request map`() {
            // given
            val bodyJson =
                "{\"xyz\":\"xyzzzxy\",\"isGenerate\":\"false\",\"contactNumber\": \"1234567890\",\"email\": \"test@gmail.com\",\"name\": \"test\",\"paymentMethod\": \"Cash\",\"productDetails\": \"[{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffeeeeeee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159},{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159}]\",\"total\": \"279\"}"

            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)
            val userDetails = TestData.getSpringUserDetails()
            jwtFilter.setCurrentUser(userDetails)

            Assertions.assertThatThrownBy {
                mockMvc.post("$BASE_URL/generate") {
                    contentType = MediaType.APPLICATION_JSON
                    content = bodyJson
                }
            }.isInstanceOf(ServletException::class.java)
        }

        @Test
        fun `should return a OK response and bill uuid while trying to generate a new bill with a correct request map`() {
            // given
            val uuid = CafeUtils.getUUID()

            billRepository.deleteByUuid(uuid)

            val bodyJson =
                "{\"uuid\":\"$uuid\",\"isGenerate\":\"false\",\"contactNumber\": \"1234567890\",\"email\": \"test@gmail.com\",\"name\": \"test\",\"paymentMethod\": \"Cash\",\"productDetails\": \"[{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffeeeeeee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159},{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159}]\",\"total\": \"279\"}"

            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)
            val userDetails = TestData.getSpringUserDetails()
            jwtFilter.setCurrentUser(userDetails)

            val resultActionsDsl = mockMvc.post("$BASE_URL/generate") {
                contentType = MediaType.APPLICATION_JSON
                content = bodyJson
            }

            resultActionsDsl.andDo { print() }.andExpect {
                status { isOk() }
                content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
            }

            Assertions.assertThat(resultActionsDsl.andReturn().response.contentAsString)
                .isEqualTo("{\"uuid:\":\" $uuid \"}")

            billRepository.deleteByUuid(uuid)
        }

    }

    @Nested
    @DisplayName("Testing web layer for getting all bills")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext
    inner class GetBillsTesting {

        @Test
        fun `should throw an exception if there is no current user logged in`() {
            // given
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)
            val userDetails = TestData.getSpringUserDetails()

            Assertions.assertThatThrownBy {
                mockMvc.get("$BASE_URL/get") {}
            }.isInstanceOf(ServletException::class.java)
        }

        @Test
        fun `should return a OK response and bill list with all bills if the current user is provided`() {
            // given
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)
            val userDetails = TestData.getSpringUserDetails()
            jwtFilter.setCurrentUser(userDetails)

            val resultActionsDsl = mockMvc.get("$BASE_URL/get") {}

            resultActionsDsl.andDo { print() }.andExpect {
                status { isOk() }
                content { MediaType.APPLICATION_JSON }
            }

            Assertions.assertThat(resultActionsDsl.andReturn().response.contentAsString)
                .contains(jwtFilter.getCurrentUser()!!.username)
        }

    }


}