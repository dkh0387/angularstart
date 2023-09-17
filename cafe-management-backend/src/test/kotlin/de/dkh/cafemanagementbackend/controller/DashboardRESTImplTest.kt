package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.entity.Authority
import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.repository.CategoryRepository
import de.dkh.cafemanagementbackend.repository.ProductRepository
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import io.mockk.clearAllMocks
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DashboardRESTImplTest {


    private val BASE_URL = "http://localhost:8081/dashboard"

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var billRepository: ProductRepository

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `should return dashboard details`() {

        // when
        val resultActionsDsl = mockMvc.get("$BASE_URL/details") {}

        // then
        val mvcResult = resultActionsDsl.andDo { print() }.andExpect {
            status { isOk() }
            content { MediaType.APPLICATION_JSON }
        }.andReturn()

        val actual = ServiceUtils.getMapFromJSON(mvcResult.response.contentAsString).entries.sortedBy { it.key }

        Assertions.assertThat(actual).isEqualTo(mapOf(
            "bill" to billRepository.count().toDouble(),
            "category" to categoryRepository.count().toDouble(),
            "product" to productRepository.count().toDouble()
        ).entries.sortedBy { it.key })

    }

    private val mapComparator = Comparator<Authority> { a1, a2 ->
        when {
            (a1 == null && a2 == null) -> 0
            (a1 == null) -> -1
            else -> User.UserRoles.valueOf(a1.authority).compareTo(User.UserRoles.valueOf(a2.authority))
        }
    }

}
