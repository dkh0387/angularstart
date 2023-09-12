package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.exception.*
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.repository.CategoryRepository
import de.dkh.cafemanagementbackend.repository.ProductRepository
import de.dkh.cafemanagementbackend.testutils.TestData
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import de.dkh.cafemanagementbackend.wrapper.ProductWrapper
import io.mockk.*
import io.mockk.junit5.MockKExtension
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

        @Nested
        @DisplayName("Testing updating products")
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        inner class UpdateProductTesting {

            @Test
            fun `should throw an exception if the productRepository does`() {
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
                every { productRepository.findById(any()) } throws RuntimeException()

                // when/then
                assertThatThrownBy { objectUnderTest.updateProduct(requestMap) }.isInstanceOf(UpdateProductException::class.java)
            }

            @Test
            fun `should return an UNAUTHORIZED response if the current user is not an admin`() {
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
                every { jwtFilter.isAdmin() } returns false
                every { productRepository.findById(any()) } returns Optional.of(product)

                // when
                val responseEntity = objectUnderTest.updateProduct(requestMap)

                // then
                verify(exactly = 0) { productRepository.save(any()) }
                assertThat(responseEntity).isEqualTo(
                    ResponseEntity<String>(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED)
                )
            }

            @Test
            fun `should return an OK response if the current user is an admin`() {
                // given
                val category = TestData.getCategory("Testcategory")
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
                every { categoryRepository.findById(product.category.id) } returns Optional.of(category)
                every { productRepository.findById(product.id) } returns Optional.of(product)
                every { productRepository.save(any()) } returns product

                // when
                val responseEntity = objectUnderTest.updateProduct(requestMap)

                // then
                verify(exactly = 1) { productRepository.save(any()) }
                assertThat(responseEntity).isEqualTo(
                    ResponseEntity<String>(CafeConstants.UPDATE_PRODUCT_SUCCESSFULLY, HttpStatus.OK)
                )
            }

        }
    }

    @Nested
    @DisplayName("Testing updating products")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class UpdateProductTesting {

        @Test
        fun `should throw an exception if the productRepository does`() {
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
            every { productRepository.findById(any()) } throws RuntimeException()

            // when/then
            assertThatThrownBy { objectUnderTest.updateProduct(requestMap) }.isInstanceOf(UpdateProductException::class.java)
        }

        @Test
        fun `should return an UNAUTHORIZED response if the current user is not an admin`() {
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
            every { jwtFilter.isAdmin() } returns false
            every { productRepository.findById(any()) } returns Optional.of(product)

            // when
            val responseEntity = objectUnderTest.updateProduct(requestMap)

            // then
            verify(exactly = 0) { productRepository.save(any()) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED)
            )
        }

        @Test
        fun `should return an OK response if the current user is an admin`() {
            // given
            val product = TestData.getProduct("Testproduct")
            val category = TestData.getCategory("Testcategory")
            val requestMap = mapOf(
                "id" to product.id.toString(),
                "name" to product.name,
                "description" to product.description,
                "status" to product.status,
                "price" to product.price.toString(),
                "categoryId" to product.category.id.toString()
            )
            every { jwtFilter.isAdmin() } returns true
            every { productRepository.findById(product.id) } returns Optional.of(product)
            every { categoryRepository.findById(category.id) } returns Optional.of(category)
            every { productRepository.save(any()) } returns product

            // when
            val responseEntity = objectUnderTest.updateProduct(requestMap)

            // then
            verify(exactly = 1) { productRepository.save(any()) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(CafeConstants.UPDATE_PRODUCT_SUCCESSFULLY, HttpStatus.OK)
            )
        }

    }

    @Nested
    @DisplayName("Testing deleting products")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteProductTesting {

        @Test
        fun `should throw an exception if the productRepository does`() {
            // given
            val product = TestData.getProduct("Testproduct")
            every { productRepository.findById(any()) } throws RuntimeException()

            // when/then
            assertThatThrownBy { objectUnderTest.deleteProduct(product.id) }.isInstanceOf(DeleteProductException::class.java)
        }

        @Test
        fun `should return an UNAUTHORIZED response if the current user is not an admin`() {
            // given
            val product = TestData.getProduct("Testproduct")
            every { jwtFilter.isAdmin() } returns false
            every { productRepository.findById(product.id) } returns Optional.of(product)

            // when
            val responseEntity = objectUnderTest.deleteProduct(product.id)

            // then
            verify(exactly = 0) { productRepository.findById(any()) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED)
            )
        }

        @Test
        fun `should return an OK response if the current user is an admin`() {
            // given
            val product = TestData.getProduct("Testproduct")
            every { jwtFilter.isAdmin() } returns true
            every { productRepository.findById(product.id) } returns Optional.of(product)
            every { productRepository.deleteById(product.id) } just runs

            // when
            val responseEntity = objectUnderTest.deleteProduct(product.id)

            // then
            verify(exactly = 1) { productRepository.deleteById(product.id) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(CafeConstants.DELETE_PRODUCT_SUCCESSFULLY, HttpStatus.OK)
            )
        }

    }

    @Nested
    @DisplayName("Testing updating product status")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class UpdateProductStatusTesting {

        @Test
        fun `should throw an exception if the productRepository does`() {
            // given
            val product = TestData.getProduct("Testproduct")
            val requestMap = mapOf(
                "id" to product.id.toString(), "status" to product.status
            )
            every { productRepository.findById(any()) } throws RuntimeException()

            // when/then
            assertThatThrownBy { objectUnderTest.updateProductStatus(requestMap) }.isInstanceOf(
                UpdateProductStatusException::class.java
            )
        }

        @Test
        fun `should return an UNAUTHORIZED response if the current user is not an admin`() {
            // given
            val product = TestData.getProduct("Testproduct")
            val requestMap = mapOf(
                "id" to product.id.toString(), "status" to product.status
            )
            every { jwtFilter.isAdmin() } returns false
            every { productRepository.findById(any()) } returns Optional.of(product)

            // when
            val responseEntity = objectUnderTest.updateProductStatus(requestMap)

            // then
            verify(exactly = 0) { productRepository.updateStatus(product.status, product.id) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED)
            )
        }

        @Test
        fun `should return an OK response if the current user is an admin`() {
            // given
            val product = TestData.getProduct("Testproduct")
            val requestMap = mapOf(
                "id" to product.id.toString(), "status" to product.status
            )
            every { jwtFilter.isAdmin() } returns true
            every { productRepository.findById(any()) } returns Optional.of(product)
            every { productRepository.updateStatus(product.status, product.id) } returns product.id.toInt()

            // when
            val responseEntity = objectUnderTest.updateProductStatus(requestMap)

            // then
            verify(exactly = 1) { productRepository.updateStatus(product.status, product.id) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(CafeConstants.UPDATE_PRODUCT_STATUS_SUCCESSFULLY, HttpStatus.OK)
            )
        }

    }

    @Nested
    @DisplayName("Testing getting all products by category with active status")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAllProductsByCategoryTesting {

        @Test
        fun `should throw an exception if the productRepository does`() {
            // given
            every { categoryRepository.findById(any()) } throws RuntimeException()

            // when/then
            assertThatThrownBy { objectUnderTest.getProductsByCategory(0) }.isInstanceOf(
                GetAllProductByCategoryException::class.java
            )
        }

        @Test
        fun `should return an OK response if a valid category is provided`() {
            // given
            val product = TestData.getProduct("Testproduct")
            val category = TestData.getCategory("Testcategory")
            every { categoryRepository.findById(any()) } returns Optional.of(category)
            every {
                productRepository.findByCategoryAndStatus(
                    category, CafeConstants.TRUE
                )
            } returns listOf(product.toWrapper())

            // when
            val responseEntity = objectUnderTest.getProductsByCategory(category.id)

            // then
            verify(exactly = 1) { productRepository.findByCategoryAndStatus(category, CafeConstants.TRUE) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<List<ProductWrapper>>(listOf(product.toWrapper()), HttpStatus.OK)
            )
        }

    }

    @Nested
    @DisplayName("Testing getting product by id")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetProductByIdTesting {

        @Test
        fun `should throw an exception if the productRepository does`() {
            // given
            every { productRepository.findById(any()) } throws RuntimeException()

            // when/then
            assertThatThrownBy { objectUnderTest.getProductById(0) }.isInstanceOf(
                GetProductByIdException::class.java
            )
        }

        @Test
        fun `should return an BAD_REQUEST response if there is no product fo privided id`() {
            // given
            val product = TestData.getProduct("Testproduct")
            every { productRepository.findById(product.id) } returns Optional.empty()

            // when
            val responseEntity = objectUnderTest.getProductById(product.id)

            // then
            verify(exactly = 1) { productRepository.findById(product.id) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<ProductWrapper>(null, HttpStatus.BAD_REQUEST)
            )
        }

        @Test
        fun `should return an OK response if a valid id is provided`() {
            // given
            val product = TestData.getProduct("Testproduct")
            every { productRepository.findById(product.id) } returns Optional.of(product)

            // when
            val responseEntity = objectUnderTest.getProductById(product.id)

            // then
            verify(exactly = 1) { productRepository.findById(product.id) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<ProductWrapper>(product.toWrapper(), HttpStatus.OK)
            )
        }

    }
}
