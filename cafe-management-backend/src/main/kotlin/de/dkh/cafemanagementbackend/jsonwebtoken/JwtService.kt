package de.dkh.cafemanagementbackend.jsonwebtoken

import io.jsonwebtoken.Claims
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.function.Function

interface JwtService {

    fun extractAllClaims(token: String): Claims
    fun extractClaims(token: String, claimsResolver: Function<Claims, Any>): Any
    fun validateToken(token: String, userDetails: UserDetails): Boolean

    fun generateToken(email: String, role: String): String

    fun extractUserName(token: String): String?

    fun extractExpiration(token: String): Date?


}