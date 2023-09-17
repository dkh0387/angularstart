package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtService
import de.dkh.cafemanagementbackend.repository.BillRepository
import de.dkh.cafemanagementbackend.testutils.TestData
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

            // when/then
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
            val bodyJson =
                "{\"contactNumber\": \"1234567890\",\"email\": \"test@gmail.com\",\"name\": \"test\",\"paymentMethod\": \"Cash\",\"productDetails\": \"[{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffeeeeeee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159},{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159}]\",\"total\": \"279\"}"

            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)
            val userDetails = TestData.getSpringUserDetails()
            jwtFilter.setCurrentUser(userDetails)

            // when
            val resultActionsDsl = mockMvc.post("$BASE_URL/generate") {
                contentType = MediaType.APPLICATION_JSON
                content = bodyJson
            }

            // then
            resultActionsDsl.andDo { print() }.andExpect {
                status { isOk() }
                content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
            }

            val uuid = billRepository.findAllAndOrderByIdDesc().first().uuid

            Assertions.assertThat(resultActionsDsl.andReturn().response.contentAsString)
                .isEqualTo("{\"uuid:\":\" $uuid \"}")

            billRepository.deleteByName("test")
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

            // when/then
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

            // when
            val resultActionsDsl = mockMvc.get("$BASE_URL/get") {}

            // then
            resultActionsDsl.andDo { print() }.andExpect {
                status { isOk() }
                content { MediaType.APPLICATION_JSON }
            }

            Assertions.assertThat(resultActionsDsl.andReturn().response.contentAsString)
                .contains(jwtFilter.getCurrentUser()!!.username)
        }

    }

    @Nested
    @DisplayName("Testing web layer for getting a bill document")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext
    inner class GetBillDocumentTesting {

        @Test
        fun `should throw an exception if there is no current user logged in`() {
            // given
            val bodyJson =
                "{\"contactNumber\": \"1234567890\",\"email\": \"test@gmail.com\",\"name\": \"test\",\"paymentMethod\": \"Cash\",\"productDetails\": \"[{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffeeeeeee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159},{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159}]\",\"total\": \"279\"}"
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            // when/then
            Assertions.assertThatThrownBy {
                mockMvc.post("$BASE_URL/getBillDocument") {
                    contentType = MediaType.APPLICATION_JSON
                    content = bodyJson
                }
            }.isInstanceOf(ServletException::class.java)
        }

        @Test
        fun `should throw an exception if a corrupt request map is provided`() {
            // given
            val bodyJson =
                "{\"contactNumber222\": \"1234567890\",\"email\": \"test@gmail.com\",\"name\": \"test\",\"paymentMethod\": \"Cash\",\"productDetails\": \"[{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffeeeeeee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159},{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159}]\",\"total\": \"279\"}"
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)
            val userDetails = TestData.getSpringUserDetails()
            jwtFilter.setCurrentUser(userDetails)

            // when/then
            Assertions.assertThatThrownBy {
                mockMvc.post("$BASE_URL/getBillDocument") {
                    contentType = MediaType.APPLICATION_JSON
                    content = bodyJson
                }
            }.isInstanceOf(ServletException::class.java)
        }

        @Test
        fun `should return an OK response and persist a new bill if a correct request map is provided`() {
            // given
            val bodyJson =
                "{\"contactNumber\": \"1234567890\",\"email\": \"test@gmail.com\",\"name\": \"test\",\"paymentMethod\": \"Cash\",\"productDetails\": \"[{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffeeeeeee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159},{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159}]\",\"total\": \"279\"}"
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)
            val userDetails = TestData.getSpringUserDetails()
            jwtFilter.setCurrentUser(userDetails)

            // when
            val resultActionsDsl = mockMvc.post("$BASE_URL/getBillDocument") {
                contentType = MediaType.APPLICATION_JSON
                content = bodyJson
            }

            // then
            resultActionsDsl.andDo { print() }.andExpect {
                status { isOk() }
                content { MediaType.APPLICATION_JSON }
            }

            Assertions.assertThat(resultActionsDsl.andReturn().response.contentAsString).contains("PDF")

            billRepository.deleteByName("test")
        }

        @Test
        fun `should return an OK response and get an exisitng bill if a correct request map with uuid is provided`() {
            // given
            val bodyJson =
                "{\"uuid\": \"filename - 2498240923\"}"
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)
            val userDetails = TestData.getSpringUserDetails()
            jwtFilter.setCurrentUser(userDetails)

            // when
            val resultActionsDsl = mockMvc.post("$BASE_URL/getBillDocument") {
                contentType = MediaType.APPLICATION_JSON
                content = bodyJson
            }

            // then
            resultActionsDsl.andDo { print() }.andExpect {
                status { isOk() }
                content { MediaType.APPLICATION_JSON }
            }

            Assertions.assertThat(resultActionsDsl.andReturn().response.contentAsString)
                .isEqualTo("lösfkjewofjeofjeqüojfqwüofjdowüq")
        }

    }

    @Nested
    @DisplayName("Testing web layer for deleting a bill")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext
    inner class DeleteBillTesting {

        @Test
        fun `should return a BAD_REQUEST response if there is no bill for provided id in the db`() {
            // given
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            // when
            val resultActionsDsl = mockMvc.post("$BASE_URL/delete/-1") {}

            // then
            resultActionsDsl.andDo { print() }.andExpect {
                status { isBadRequest() }
                content { MediaType.APPLICATION_JSON }
            }

            Assertions.assertThat(resultActionsDsl.andReturn().response.contentAsString)
                .isEqualTo(CafeConstants.DELETE_BILL_WENT_WRONG)
        }

        @Test
        fun `should return an OK response if there is a bill for provided id in the db`() {
            // given
            val bill = TestData.getBill()
            bill.document = ByteArray(100)
            val billSaved = billRepository.save(bill)


            // when
            val resultActionsDsl = mockMvc.post("$BASE_URL/delete/${billSaved.id}") {}

            // then
            resultActionsDsl.andDo { print() }.andExpect {
                status { isOk() }
                content { MediaType.APPLICATION_JSON }
            }

            Assertions.assertThat(resultActionsDsl.andReturn().response.contentAsString)
                .isEqualTo(CafeConstants.DELETE_BILL_SUCCESSFULLY)
        }

    }


}