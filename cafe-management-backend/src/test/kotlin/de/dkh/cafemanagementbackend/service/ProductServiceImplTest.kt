package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.exception.AddProductException
import de.dkh.cafemanagementbackend.exception.GetAllProductException
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.repository.CategoryRepository
import de.dkh.cafemanagementbackend.repository.ProductRepository
import de.dkh.cafemanagementbackend.testutils.TestData
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import de.dkh.cafemanagementbackend.wrapper.ProductWrapper
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
import java.util.*

@ExtendWith(MockKExtension::class)
class ProductServiceImplTest {

    private val productRepository = mockk<ProductRepository>()
    private val categoryRepository = mockk<CategoryRepository>()
    private val jwtFilter = mockk<JwtFilter>()
    private val objectUnderTest = ProductServiceImpl(productRepository, categoryRepository, jwtFilter)

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
                "id" to product.id.toString(),
                "name" to product.name,
                "description" to product.description,
                "status" to product.status,
                "price" to product.price.toString(),
                "categoryId" to product.category.id.toString()
            )
            every { jwtFilter.isAdmin() } returns true
            every { productRepository.findById(any()) } returns Optional.of(TestData.getProduct("Testproduct"))
            every { categoryRepository.findById(any()) } returns Optional.of(TestData.getCategory("Testcategory"))
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

    @Nested
    @DisplayName("Testing getting all products")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetProductTesting {

        @Test
        fun `should throw an exception if the productRepository does`() {
            // given
            every { productRepository.getAllProduct() } throws RuntimeException()

            // when/then
            assertThatThrownBy { objectUnderTest.getAllProduct() }.isInstanceOf(GetAllProductException::class.java)
        }

        @Test
        fun `should return an UNAUTHORIZED response if the current user is not an admin`() {
            // given
            every { jwtFilter.isAdmin() } returns false

            // when
            val responseEntity = objectUnderTest.getAllProduct()

            // then
            verify(exactly = 0) { productRepository.getAllProduct() }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<List<ProductWrapper>>(
                    emptyList(), HttpStatus.UNAUTHORIZED
                )
            )
        }

        @Test
        fun `should return an OK response if the current user is an admin`() {
            // given
            every { jwtFilter.isAdmin() } returns true
            every { productRepository.getAllProduct() } returns listOf(TestData.getProduct("Testproduct").toWrapper())

            // when
            val responseEntity = objectUnderTest.getAllProduct()

            // then
            verify(exactly = 1) { productRepository.getAllProduct() }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<List<ProductWrapper>>(
                    listOf(TestData.getProduct("Testproduct").toWrapper()), HttpStatus.OK
                )
            )
        }

    }
}