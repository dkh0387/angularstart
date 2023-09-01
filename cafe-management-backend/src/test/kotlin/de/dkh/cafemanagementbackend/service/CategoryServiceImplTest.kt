package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.exception.AddCategoryException
import de.dkh.cafemanagementbackend.exception.UpdateCategoryException
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.repository.CategoryRepository
import de.dkh.cafemanagementbackend.testutils.TestData
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
class CategoryServiceImplTest {

    private val categoryRepository = mockk<CategoryRepository>()
    private val jwtFilter = mockk<JwtFilter>()
    private val objectUnderTest = CategoryServiceImpl(categoryRepository, jwtFilter)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Nested
    @DisplayName("Testing adding a new category")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class AddCategoryTesting {

        @Test
        fun `should throw an exception if requested map does contains wrong fields`() {
            // given
            val category = TestData.getCategory("Pasta")
            val requestMap = mapOf("name2" to category.name)

            // when/then
            assertThatThrownBy { objectUnderTest.addCategory(requestMap) }.isInstanceOf(AddCategoryException::class.java)
        }

        @Test
        fun `should throw an exception if categoryRepository does`() {
            // given
            val category = TestData.getCategory("Pasta")
            val requestMap = mapOf("name" to category.name)
            every { categoryRepository.save(any()) } throws Exception()

            // when/then
            assertThatThrownBy { objectUnderTest.addCategory(requestMap) }.isInstanceOf(AddCategoryException::class.java)
        }

        @Test
        fun `should return an UNAUTHORIZED_ACCESS response if current user is not an admin`() {
            // given
            val category = TestData.getCategory("Pasta")
            val requestMap = mapOf("name" to category.name)
            every { jwtFilter.isAdmin() } returns false

            // when
            val responseEntity = objectUnderTest.addCategory(requestMap)

            // then
            verify(exactly = 0) { categoryRepository.save(category) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED
                )
            )
        }

        @Test
        fun `should return an ADD_CATEGORY_SUCCESSFULLY response and save category if current user is an admin`() {
            // given
            val category = TestData.getCategory("Pasta")
            val requestMap = mapOf("name" to category.name)
            every { jwtFilter.isAdmin() } returns true
            every { categoryRepository.save(category) } returns category

            // when
            val responseEntity = objectUnderTest.addCategory(requestMap)

            // then
            verify(exactly = 1) { categoryRepository.save(category) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.ADD_CATEGORY_SUCCESSFULLY, HttpStatus.OK
                )
            )
        }

    }

    @Nested
    @DisplayName("Testing get all category")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAllCategoryTesting {

        @Test
        fun `should return all categories if everything ok and no filter value provided`() {
            // given
            every { categoryRepository.findAll() } returns listOf(TestData.getCategory("Pasta"))


            // when
            val responseEntity = objectUnderTest.getAllCategory(null)

            // then
            assertThat(responseEntity.statusCode.is2xxSuccessful)
            assertThat(responseEntity.body).isEqualTo(listOf(TestData.getCategory("Pasta").toWrapper()))
        }

        /**
         * @TODO: probably the test should be modified if other logic for filtering will be implemented!
         */
        @Test
        fun `should return filtered categories if everything ok and a filter value provided`() {
            // given
            every { categoryRepository.getAllCategory() } returns listOf(TestData.getCategory("Pasta"))


            // when
            val responseEntity = objectUnderTest.getAllCategory("true")

            // then
            assertThat(responseEntity.statusCode.is2xxSuccessful)
            assertThat(responseEntity.body).isEqualTo(listOf(TestData.getCategory("Pasta").toWrapper()))
        }

    }

    @Nested
    @DisplayName("Testing update category")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class UpdateCategoryTesting {

        @Test
        fun `should throw an exception if the provided request map contains wrong fields`() {
            // given
            val category = TestData.getCategory("Pasta")
            val requestMap = mapOf("name2" to category.name)

            // when/then
            assertThatThrownBy { objectUnderTest.updateCategory(requestMap) }.isInstanceOf(UpdateCategoryException::class.java)
        }

        @Test
        fun `should return a UPDATE_CATEGORY_WENT_WRONG response if the provided request map does not contain an id`() {
            // given
            val category = TestData.getCategory("Pasta")
            val requestMap = mapOf("name" to category.name)

            every { jwtFilter.isAdmin() } returns true

            // when
            val responseEntity = objectUnderTest.updateCategory(requestMap)

            // then
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.UPDATE_CATEGORY_WENT_WRONG, HttpStatus.BAD_REQUEST
                )
            )
        }

        @Test
        fun `should return a UNAUTHORIZED_ACCESS response if the current user is not an admin`() {
            // given
            val category = TestData.getCategory("Pasta")
            val requestMap = mapOf("id" to "1", "name" to category.name)

            every { jwtFilter.isAdmin() } returns false
            every { categoryRepository.findById(1) } returns Optional.of(TestData.getCategory("Pasta"))

            // when
            val responseEntity = objectUnderTest.updateCategory(requestMap)

            // then
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED
                )
            )
        }

        @Test
        fun `should return a UPDATE_CATEGORY_WENT_WRONG response if there is no category for provided id`() {
            // given
            val category = TestData.getCategory("Pasta")
            val requestMap = mapOf("id" to "11111", "name" to category.name)

            every { jwtFilter.isAdmin() } returns true
            every { categoryRepository.findById(11111) } returns Optional.empty()

            // when
            val responseEntity = objectUnderTest.updateCategory(requestMap)

            // then
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.UPDATE_CATEGORY_WENT_WRONG, HttpStatus.BAD_REQUEST
                )
            )
        }

        @Test
        fun `should return a UPDATE_CATEGORY_SUCCESSFULLY response if there is a category for provided id`() {
            // given
            val category = TestData.getCategory("Pasta")
            val requestMap = mapOf("id" to "1", "name" to category.name)

            every { jwtFilter.isAdmin() } returns true
            every { categoryRepository.findById(1) } returns Optional.of(TestData.getCategory("Pasta"))
            every { categoryRepository.save(TestData.getCategory("Pasta")) } returns TestData.getCategory("Pasta")

            // when
            val responseEntity = objectUnderTest.updateCategory(requestMap)

            // then
            verify(exactly = 1) { categoryRepository.save(any()) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    CafeConstants.UPDATE_CATEGORY_SUCCESSFULLY, HttpStatus.OK
                )
            )
        }
    }
}