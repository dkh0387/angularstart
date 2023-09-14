package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.exception.GenerateBillException
import de.dkh.cafemanagementbackend.exception.GetBillsException
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.repository.BillRepository
import de.dkh.cafemanagementbackend.testutils.TestData
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import de.dkh.cafemanagementbackend.wrapper.BillWrapper
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
class BillServiceImplTest {

    private val billRepository = mockk<BillRepository>()
    private val jwtFilter = mockk<JwtFilter>()
    private val billCreator = mockk<BillCreator>()
    private val objectUnderTest = BillServiceImpl(billRepository, jwtFilter, billCreator)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Nested
    @DisplayName("Testing creating a new bill")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class CreatingBillTesting {

        @Test
        fun `should throw an exception if the provided request map does not contain write keys`() {
            // given
            val bodyJson =
                "{\"xyz\":\"xyzzzxy\",\"contactNumber\": \"1234567890\",\"email\": \"test@gmail.com\",\"name\": \"test\",\"paymentMethod\": \"Cash\",\"productDetails\": \"[{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffeeeeeee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159},{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159}]\",\"totalAmount\": \"279\"}"
            val requestMap = ServiceUtils.getMapFromJSON(bodyJson)

            // when/then
            assertThatThrownBy { objectUnderTest.generateBill(requestMap) }.isInstanceOf(GenerateBillException::class.java)
        }

        @Test
        fun `should return a OK response with a uuid from request if the provided request map does contain write keys`() {
            // given
            val bodyJson =
                "{\"uuid\":\"xyzzzxy\",\"isGenerate\":\"false\",\"contactNumber\": \"1234567890\",\"email\": \"test@gmail.com\",\"name\": \"test\",\"paymentMethod\": \"Cash\",\"productDetails\": \"[{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffeeeeeee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159},{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159}]\",\"total\": \"279\"}"
            val requestMap = ServiceUtils.getMapFromJSON(bodyJson)
            val bill = TestData.getBill()
            every { jwtFilter.getCurrentUser() } returns TestData.getSpringUserDetails()
            every { billRepository.save(any()) } returns bill
            every { billCreator.createAndPersist(any(), any()) } returns ByteArray(100)

            // when
            val responseEntity = objectUnderTest.generateBill(requestMap)

            // then
            verify(exactly = 1) { billRepository.save(any()) }
            verify(exactly = 1) { billCreator.createAndPersist(any(), any()) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<String>(
                    "{\"uuid:\":\" xyzzzxy \"}", HttpStatus.OK
                )
            )
        }

    }

    @Nested
    @DisplayName("Testing getting all bills")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GettingAllBillsTesting {

        @Test
        fun `should throw an exception if the bill repository does`() {
            // given
            every { billRepository.findAllAndOrderByIdDesc() } throws RuntimeException()

            // when/then
            assertThatThrownBy { objectUnderTest.getBills() }.isInstanceOf(GetBillsException::class.java)
        }

        @Test
        fun `should return all bills if the current user is an admin`() {
            // given
            every { billRepository.findAllAndOrderByIdDesc() } returns listOf(TestData.getBill())
            every { jwtFilter.isAdmin() } returns true

            // when
            val responseEntity = objectUnderTest.getBills()

            // then
            verify(exactly = 1) { billRepository.findAllAndOrderByIdDesc() }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<List<BillWrapper>>(
                    listOf(TestData.getBill().toWrapper()), HttpStatus.OK
                )
            )
        }

        @Test
        fun `should return all bills for the current user if the current user is not an admin`() {
            // given
            every { billRepository.findAllByNameOrderByNameDesc(any()) } returns listOf(TestData.getBill())
            every { jwtFilter.getCurrentUser() } returns TestData.getSpringUserDetails()
            every { jwtFilter.isAdmin() } returns false

            // when
            val responseEntity = objectUnderTest.getBills()

            // then
            verify(exactly = 1) { billRepository.findAllByNameOrderByNameDesc(any()) }
            assertThat(responseEntity).isEqualTo(
                ResponseEntity<List<BillWrapper>>(
                    listOf(TestData.getBill().toWrapper()), HttpStatus.OK
                )
            )
        }

    }

}