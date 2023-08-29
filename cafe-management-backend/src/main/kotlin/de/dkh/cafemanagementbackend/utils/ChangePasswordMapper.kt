package de.dkh.cafemanagementbackend.utils

import com.fasterxml.jackson.annotation.JsonAutoDetect
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
@AllArgsConstructor
class ChangePasswordMapper() : KeyMapper() {

    var oldPassword: String = ""
    var newPassword: String = ""

}