package de.dkh.cafemanagementbackend.utils.mapper

import com.fasterxml.jackson.annotation.JsonAutoDetect
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
@AllArgsConstructor
class BillMapper : KeyMapper() {

    var uuid: String = ""
    var name: String = ""
    var contactNumber: String = ""
    var email: String = ""
    var paymentMethod: String = ""
    var productDetails: String = ""
    var total: Double = 0.0
    var isGenerate: Boolean? = null

}