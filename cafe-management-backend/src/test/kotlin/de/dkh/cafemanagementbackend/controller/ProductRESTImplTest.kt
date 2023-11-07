package de.dkh.cafemanagementbackend.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtService
import de.dkh.cafemanagementbackend.repository.CategoryRepository
import de.dkh.cafemanagementbackend.repository.ProductRepository
import de.dkh.cafemanagementbackend.testutils.TestData
import de.dkh.cafemanagementbackend.utils.CafeUtils
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import io.mockk.clearAllMocks
import jakarta.servlet.ServletException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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

        @Test
        fun `should throw an exception while trying to save a new product with a corrupt request map`() {
            // given
            val productMapperMap = mapOf(
                "name2" to "Testproduct", "description" to "Testdescription", "status" to "true", "categoryId" to "1"
            )
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            // when
            val resultActionsDsl = (mockMvc.post("$BASE_URL/add") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(productMapperMap)
            })

            // then
            val mvcResult = resultActionsDsl.andDo { print() }.andExpect {
                status { isInternalServerError() }
                content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
            }.andReturn()

            assertThat(mvcResult.response.contentAsString).isEqualTo(CafeConstants.ADD_PRODUCT_WENT_WRONG)

        }

        @Test
        fun `should throw an exception while trying to save a new product without category`() {

            productRepository.deleteByName("Testproduct")

            // given
            val productMapperMap = mapOf(
                "name" to "Testproduct", "description" to "Testdescription", "status" to "true", "categoryId" to "-1"
            )
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            // when
            val resultActionsDsl = (mockMvc.post("$BASE_URL/add") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(productMapperMap)
            })

            // then
            val mvcResult = resultActionsDsl.andDo { print() }.andExpect {
                status { isInternalServerError() }
                content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
            }.andReturn()

            assertThat(mvcResult.response.contentAsString).isEqualTo(CafeConstants.ADD_PRODUCT_WENT_WRONG)

        }

        @Test
        fun `should save a new product if the request map is correctly provided`() {

            productRepository.deleteByName("Testproduct")

            // given
            val productMapperMap = mapOf(
                "name" to "Testproduct", "description" to "Testdescription", "status" to "true", "categoryId" to "1"
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

    }

    @Nested
    @DisplayName("Testing web layer for getting all products")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext
    inner class GettingAllProducts {

        @Test
        fun `should return an UNAUTHORIZED response if the current user is not an admin`() {
            // given
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            // when
            val resultActionsDsl = mockMvc.get("$BASE_URL/get") {}

            // then
            resultActionsDsl.andDo { print() }.andExpect {
                status { isUnauthorized() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }
        }

        @Test
        fun `should return an OK response if the current user is an admin`() {
            // given
            val productOptional = productRepository.findByName("Small salat")
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            // when
            val resultActionsDsl = mockMvc.get("$BASE_URL/get") {}

            // then
            val mvcResult = resultActionsDsl.andDo { print() }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }.andReturn()

            val contentAsString = mvcResult.response.contentAsString
            assertThat(contentAsString).contains(TestData.getProductWrapperJson(productOptional.get().id))
        }

    }

    @Nested
    @DisplayName("Testing web layer for updating a product")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext
    inner class UpdatingProduct {

        @Test
        fun `should throw an exception while trying to update a product with a corrupt request map`() {
            // given
            val productMapperMap = mapOf("id" to "1", "status2" to "true")
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            assertThatThrownBy {
                mockMvc.post("$BASE_URL/update") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(productMapperMap)
                }
            }.isInstanceOf(ServletException::class.java)

        }

        @Test
        fun `should return a UNAUTHORIZED response while trying to update a product as non admin`() {
            // given
            val productMapperMap = mapOf("id" to "1", "status" to "true")
            val token = jwtService.generateToken("david@luv2code.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = mockMvc.post("$BASE_URL/update") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(productMapperMap)
            }

            resultActionsDsl.andDo { print() }.andExpect {
                status { isUnauthorized() }
                content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
            }

            assertThat(resultActionsDsl.andReturn().response.contentAsString).isEqualTo(
                CafeUtils.formatBodyAsJSON(
                    CafeConstants.UNAUTHORIZED_ACCESS
                )
            )
        }

        @Test
        fun `should return a UPDATE_PRODUCT_WENT_WRONG response while trying to update a product without status`() {

            productRepository.deleteByName("Testproduct")

            // given
            val productMapperMap = mapOf("id" to "1")
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = mockMvc.post("$BASE_URL/update") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(productMapperMap)
            }

            resultActionsDsl.andDo { print() }.andExpect {
                status { isBadRequest() }
                content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
            }

        }

        @Test
        fun `should update a product if the request map is correctly provided`() {
            val category = TestData.getCategory("Testcategory")
            val savedCategory = categoryRepository.save(category)
            val product = TestData.getProduct("Testproduct")
            product.category = category
            val savedProduct = productRepository.save(product)
            // given
            val productMapperMap =
                mapOf("id" to "${savedProduct.id}", "status" to "true", "categoryId" to "${savedCategory.id}")
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = mockMvc.post("$BASE_URL/update") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(productMapperMap)
            }

            resultActionsDsl.andDo { print() }.andExpect {
                status { isOk() }
                content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
            }
            categoryRepository.deleteByName("Testcategory")
        }

    }

    @Nested
    @DisplayName("Testing web layer for deleting a product")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext
    inner class DeletingProduct {

        @Test
        fun `should return a DELETE_PRODUCT_WENT_WRONG response while trying to delete a product with non existing id`() {
            // given
            val id = -1
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = mockMvc.post("$BASE_URL/delete/$id") {}

            resultActionsDsl.andDo { print() }.andExpect {
                status { isBadRequest() }
                content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
            }

            assertThat(resultActionsDsl.andReturn().response.contentAsString).isEqualTo(
                CafeUtils.formatBodyAsJSON(
                    CafeConstants.DELETE_PRODUCT_WENT_WRONG
                )
            )
        }

        @Test
        fun `should return a UNAUTHORIZED response while trying to delete a product as non admin`() {
            // given
            val id = 1
            val token = jwtService.generateToken("david@luv2code.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = mockMvc.post("$BASE_URL/delete/$id") {}

            resultActionsDsl.andDo { print() }.andExpect {
                status { isUnauthorized() }
                content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
            }

            assertThat(resultActionsDsl.andReturn().response.contentAsString).isEqualTo(
                CafeUtils.formatBodyAsJSON(
                    CafeConstants.UNAUTHORIZED_ACCESS
                )
            )
        }

        @Test
        fun `should delete a product if the request map is correctly provided`() {
            val category = TestData.getCategory("Testcategory")
            val savedCategory = categoryRepository.save(category)
            val product = TestData.getProduct("Testproduct")
            product.category = savedCategory
            val savedProduct = productRepository.save(product)
            // given
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = mockMvc.post("$BASE_URL/delete/${savedProduct.id}") {}

            resultActionsDsl.andDo { print() }.andExpect {
                status { isOk() }
                content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
            }
            categoryRepository.deleteByName("Testcategory")
        }

    }

    @Nested
    @DisplayName("Testing web layer for updating product status")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext
    inner class UpdatingProductStatus {

        @Test
        fun `should throw an exception while trying to update product status with a corrupt request map`() {
            // given
            val productMapperMap = mapOf("id" to "1", "status2" to "true")
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            assertThatThrownBy {
                mockMvc.post("$BASE_URL/updateStatus") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(productMapperMap)
                }
            }.isInstanceOf(ServletException::class.java)

        }

        @Test
        fun `should return a UNAUTHORIZED response while trying to update product status as non admin`() {
            // given
            val productMapperMap = mapOf("id" to "1", "status" to "true")
            val token = jwtService.generateToken("david@luv2code.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = mockMvc.post("$BASE_URL/updateStatus") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(productMapperMap)
            }

            resultActionsDsl.andDo { print() }.andExpect {
                status { isUnauthorized() }
                content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
            }

            assertThat(resultActionsDsl.andReturn().response.contentAsString).isEqualTo(CafeConstants.UNAUTHORIZED_ACCESS)
        }

        @Test
        fun `should return a UPDATE_PRODUCT_STATUS_WENT_WRONG response while trying to update product status without status`() {

            productRepository.deleteByName("Testproduct")

            // given
            val productMapperMap = mapOf("id" to "1")
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = mockMvc.post("$BASE_URL/updateStatus") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(productMapperMap)
            }

            resultActionsDsl.andDo { print() }.andExpect {
                status { isBadRequest() }
                content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
            }
        }

        @Test
        fun `should update product status if the request map is correctly provided`() {
            val category = TestData.getCategory("Testcategory")
            val savedCategory = categoryRepository.save(category)
            val product = TestData.getProduct("Testproduct")
            product.category = savedCategory
            val savedProduct = productRepository.save(product)
            // given
            val productMapperMap = mapOf("id" to "${savedProduct.id}", "status" to "true")
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = mockMvc.post("$BASE_URL/updateStatus") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(productMapperMap)
            }

            resultActionsDsl.andDo { print() }.andExpect {
                status { isOk() }
                content { contentType(MediaType("text", "plain", StandardCharsets.UTF_8)) }
            }
            categoryRepository.deleteByName("Testcategory")
        }

    }

    @Nested
    @DisplayName("Testing web layer for getting all active products by category")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DirtiesContext
    inner class GettingAllActiveProductsByCategory {

        @Test
        fun `should throw an exception while trying to get products status with a corrupt category id`() {
            // given
            val categoryId = -1
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = mockMvc.get("$BASE_URL/getByCategory/$categoryId") {}

            resultActionsDsl.andDo { print() }.andExpect {
                status { isBadRequest() }
                content { MediaType.APPLICATION_JSON }
            }

        }

        @Test
        fun `should get products by category if category id is correctly provided`() {
            val category = TestData.getCategory("Testcategory")
            val savedCategory = categoryRepository.save(category)
            val product = TestData.getProduct("Testproduct")
            product.category = savedCategory
            val savedProduct = productRepository.save(product)
            // given
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_ADMIN.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = mockMvc.get("$BASE_URL/getByCategory/${savedCategory.id}") {}

            val mvcResult = resultActionsDsl.andDo { print() }.andExpect {
                status { isOk() }
                content { MediaType.APPLICATION_JSON }
            }.andReturn()

            assertThat(mvcResult.response.contentAsString).contains(
                ServiceUtils.objectMapper.writeValueAsString(savedProduct.id)
            )
            assertThat(mvcResult.response.contentAsString).contains(
                ServiceUtils.objectMapper.writeValueAsString(savedProduct.name)
            )
            assertThat(mvcResult.response.contentAsString).contains(
                ServiceUtils.objectMapper.writeValueAsString(savedCategory.id)
            )
            assertThat(mvcResult.response.contentAsString).contains(
                ServiceUtils.objectMapper.writeValueAsString(savedCategory.name)
            )
            categoryRepository.deleteByName("Testcategory")
        }

    }

    @Nested
    @DisplayName("Testing web layer for getting a product by id")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GettingProductByIdTesting {

        @Test
        fun `should return a BAD_REQUEST if there is no product for provided id`() {
            // given
            val id = -1
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = mockMvc.get("$BASE_URL/getById/$id") {}

            resultActionsDsl.andDo { print() }.andExpect {
                status { isBadRequest() }
                content { MediaType.APPLICATION_JSON }
            }
        }

        @Test
        fun `should throw an exception if the productRepository does`() {
            // given
            val product = productRepository.findAll().first()
            val id = product.id
            val token = jwtService.generateToken("deniskh87@gmail.com", User.UserRoles.ROLE_USER.name)
            jwtFilter.claims = jwtService.extractAllClaims(token)

            val resultActionsDsl = mockMvc.get("$BASE_URL/getById/$id") {}

            val mvcResult = resultActionsDsl.andDo { print() }.andExpect {
                status { isOk() }
                content { MediaType.APPLICATION_JSON }
            }.andReturn()

            assertThat(mvcResult.response.contentAsString).contains(product.id.toString())
            assertThat(mvcResult.response.contentAsString).contains(product.name)
            assertThat(mvcResult.response.contentAsString).contains(product.category.id.toString())
            assertThat(mvcResult.response.contentAsString).contains(product.category.name)
        }
    }
}
