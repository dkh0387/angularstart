package de.dkh.cafemanagementbackend.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtService
import de.dkh.cafemanagementbackend.repository.CategoryRepository
import de.dkh.cafemanagementbackend.repository.ProductRepository
import io.mockk.clearAllMocks
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
//@ActiveProfiles(value = ["test"])
class ProductRESTImplTest {

    private val BASE_URL = "http://localhost:8081/product"

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var categoryRepository: CategoryRepository

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
    @DisplayName("Testing web layer for adding a new product")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext
    inner class AddingProduct {
        /**
         * @TODO: deserialization problems with Product-Categroy relation!
         */
        @Test
        fun `should throw an exception while trying to save a new product without category`() {

            productRepository.deleteByName("Testproduct")

            // given
            val productMapperMap = mapOf(
                "name" to "Testproduct",
                "description" to "Testdescription",
                "status" to "true",
                "categoryId" to "1"
            )
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = mockMvc.post("$BASE_URL/add") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(productMapperMap)
            }

            resultActionsDsl.andDo { print() }.andExpect {
                status { isOk() }
                content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
            }

            productRepository.deleteByName("Testproduct")
        }

        /*       @Test
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

                   Assertions.assertThatThrownBy {
                       (mockMvc.post("$BASE_URL/signup") {
                           contentType = MediaType.APPLICATION_JSON
                           val newUser = TestData.getInactiveUser().copy(email = "fjgroltkoksd")
                           content = objectMapper.writeValueAsString(newUser)
                       })
                   }.isInstanceOf(ServletException::class.java)
               }*/

    }
}