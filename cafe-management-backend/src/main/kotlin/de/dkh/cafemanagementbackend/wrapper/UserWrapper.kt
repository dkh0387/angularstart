package de.dkh.cafemanagementbackend.wrapper

import lombok.NoArgsConstructor
import lombok.ToString

@NoArgsConstructor
@ToString
data class UserWrapper(
    val id: Long,
    val name: String,
    val email: String,
    val contactNumber: String,
    val status: String
) {

}
