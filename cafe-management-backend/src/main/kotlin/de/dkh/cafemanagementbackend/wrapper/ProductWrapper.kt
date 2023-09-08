package de.dkh.cafemanagementbackend.wrapper

import com.fasterxml.jackson.annotation.JsonAutoDetect
import lombok.NoArgsConstructor
import lombok.ToString

@NoArgsConstructor
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class ProductWrapper(
    private val id: Long,
    private val name: String,
    private val description: String,
    private val price: Double,
    private val status: String,
    private val categoryId: Long,
    private val categoryName: String
) {
}