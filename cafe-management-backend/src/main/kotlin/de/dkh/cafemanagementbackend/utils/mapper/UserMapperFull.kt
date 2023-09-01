package de.dkh.cafemanagementbackend.utils.mapper

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.dkh.cafemanagementbackend.entity.Authority
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
@AllArgsConstructor
class UserMapperFull() : UserMapperSimple() {

    var id: Long? = null
    var status: String = ""
    var role: String = ""
    var authorities: List<Authority>? = emptyList()

}