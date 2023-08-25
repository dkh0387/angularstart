package de.dkh.cafemanagementbackend.wrapper

import com.fasterxml.jackson.annotation.JsonAutoDetect
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.ToString

@NoArgsConstructor
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class UserWrapper(
    private val id: Long,
    private val name: String,
    val email: String,
    private val contactNumber: String,
    private val status: String
) {

}
