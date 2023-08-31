package de.dkh.cafemanagementbackend.utils.mapper

import com.fasterxml.jackson.annotation.JsonAutoDetect
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
@AllArgsConstructor
class UserMapperFull() : UserMapperSimple() {

    var id: Long? = null
    var status: String = ""
    var role: String = ""

}