package de.dkh.cafemanagementbackend.utils

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
@AllArgsConstructor
open class UserMapperSimple() : KeyMapper() {

    var name: String = ""
    var contactNumber: String = ""
    var email: String = ""
    var password: String = ""
}