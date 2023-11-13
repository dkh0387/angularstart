package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.testutils.TestData
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.json.JSONException
import org.junit.jupiter.api.Test

class BillCreatorTest {

    private val objectUnderTest = BillCreator()

    @Test
    fun `should throw an exception if the bill mapper object contains corrupt product details`() {
        // given
        val bill = TestData.getBill()

        // when/then
        assertThatThrownBy { objectUnderTest.createAndPersist(bill.toBillMapper(), "testHeader") }.isInstanceOf(
            JSONException::class.java
        )
    }

    @Test
    fun `should return a byte array from generated bill document if everything is ok`() {
        // given
        val bodyJson =
            "{\"uuid\":\"xyzzzxy\",\"isGenerate\":\"false\",\"contactNumber\": \"1234567890\",\"email\": \"test@gmail.com\",\"name\": \"test\",\"paymentMethod\": \"Cash\",\"productDetails\": \"[{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffeeeeeee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159},{\\\"id\\\":18,\\\"name\\\":\\\"Doppio Coffee\\\",\\\"category\\\":\\\"Coffee\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":120,\\\"total\\\":120},{\\\"id\\\":5,\\\"name\\\":\\\"Chocolate Frosted Doughnut\\\",\\\"category\\\":\\\"Doughnut\\\",\\\"quantity\\\":\\\"1\\\",\\\"price\\\":159,\\\"total\\\":159}]\",\"total\": \"279\"}"
        val requestMap = ServiceUtils.getMapFromJSON(bodyJson)
        val bill = TestData.getBill().copy(productDetails = requestMap["productDetails"] as String)

        // when
        val bytes = objectUnderTest.createAndPersist(bill.toBillMapper(), "testHeader")

        // then
        assertThat(bytes).isNotEmpty()
    }
}