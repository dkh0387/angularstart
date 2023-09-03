package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.exception.AddProductException
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.repository.ProductRepository
import de.dkh.cafemanagementbackend.testutils.TestData
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@ExtendWith(MockKExtension::class)
class ProductServiceImplTest {

    private val productRepository = mockk<ProductRepository>()
    private val jwtFilter = mockk<JwtFilter>()
    private val objectUnderTest = ProductServiceImpl(productRepository, jwtFilter)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Nested
    @DisplayName("Testing adding a new product")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class AddProductTesting {

        @Test
        fun `should throw a AddProductException if the request map does not contains right fields`() {
            // given
            val product = TestData.getProduct("Testproduct")
            val requestMap = mapOf("name2" to product.name)

            // when / then
            assertThatThrownBy { objectUnderTest.addProduct(requestMap) }.isInstanceOf(AddProductException::class.java)
        }

        @Test
        fun `should return a UNAUTHORIZED_ACCESS response if the current user is no an admin`() {
            // given
            val product = TestData.getProduct("Testproduct")
            val requestMap = mapOf(
                "name" to product.name,
                "description" to product.description,
                "status" to product.status,
                "price" to product.price.toString(),
                "category" to ServiceUtils.objectMapper.writeValueAsString(product.category)
            )
            every { jwtFilter.isAdmin() } returns false

            // when
            val responseEntity = objectUnderTest.addProduct(requestMap)

            // then
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED
                )
            )
        }

        @Test
        fun `should return a OK response if the current user is an admin`() {
            // given
            val product = TestData.getProduct("Testproduct")
            val requestMap = mapOf(
                "name" to product.name,
                "description" to product.description,
                "status" to product.status,
                "price" to product.price.toString(),
                "category" to ServiceUtils.objectMapper.writeValueAsString(product.category)
            )
            every { jwtFilter.isAdmin() } returns true
            every { productRepository.save(product) } returns product

            // when
            val responseEntity = objectUnderTest.addProduct(requestMap)

            // then
            verify(exactly = 1) { productRepository.save(product) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.ADD_PRODUCT_SUCCESSFULLY, HttpStatus.OK
                )
            )
        }

    }
}