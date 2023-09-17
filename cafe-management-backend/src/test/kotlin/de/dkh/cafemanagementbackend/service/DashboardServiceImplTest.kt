package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.exception.GetDashboardDetailsException
import de.dkh.cafemanagementbackend.repository.BillRepository
import de.dkh.cafemanagementbackend.repository.CategoryRepository
import de.dkh.cafemanagementbackend.repository.ProductRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus

@ExtendWith(MockKExtension::class)
class DashboardServiceImplTest {

    private val productRepository = mockk<ProductRepository>()
    private val categoryRepository = mockk<CategoryRepository>()
    private val billRepository = mockk<BillRepository>()
    private val objectUnderTest = DashboardServiceImpl(categoryRepository, productRepository, billRepository)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `should throw an exception if any repository does`() {
        // given
        every { categoryRepository.count() } throws RuntimeException()

        // when/then
        assertThatThrownBy { objectUnderTest.getDetails() }.isInstanceOf(GetDashboardDetailsException::class.java)

    }

    @Test
    fun `should return an OK response with all details if all repositories returns count infos`() {
        // given
        every { categoryRepository.count() } returns 1
        every { productRepository.count() } returns 2
        every { billRepository.count() } returns 3

        // when
        val responseEntity = objectUnderTest.getDetails()

        // then
        verify(exactly = 1) { productRepository.count() }
        verify(exactly = 1) { categoryRepository.count() }
        verify(exactly = 1) { billRepository.count() }

        assertThat(responseEntity.body).isEqualTo(
            mapOf(
                "category" to 1L, "product" to 2L, "bill" to 3L
            )
        )
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
    }
}