package de.dkh.cafemanagementbackend.exception

data class SignUpErrorResponce(
    private val name: String,
    private val localizedMessage: String,
    private val currentTimeMillis: Long
) {
}