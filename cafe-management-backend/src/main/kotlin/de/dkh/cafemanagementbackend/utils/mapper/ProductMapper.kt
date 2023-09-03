package de.dkh.cafemanagementbackend.utils.mapper

import com.fasterxml.jackson.annotation.JsonAutoDetect
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
@AllArgsConstructor
class ProductMapper() : KeyMapper() {

    var id: Long? = null
    var name: String = ""
    var description: String = ""
    var price: Double = 0.0
    var status: String = ""

}