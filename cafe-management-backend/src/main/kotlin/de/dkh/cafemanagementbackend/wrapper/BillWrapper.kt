package de.dkh.cafemanagementbackend.wrapper

import com.fasterxml.jackson.annotation.JsonAutoDetect
import lombok.NoArgsConstructor
import lombok.ToString

@NoArgsConstructor
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class BillWrapper(
    private val id: Long,
    private val uuid: String,
    private val name: String,
    private val email: String,
    private val contactNumber: String,
    private val paymentMethod: String,
    private val total: Double,
    private val productDetails: String,
    private val createdBy: String
) {

}
