package de.dkh.cafemanagementbackend.jsonwebtoken

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.function.Function

/**
 * Service for JSON web tokens.
 * The whole process is as follows:
 * 1. Client calls POST .../user/signup with user credentials as JSON
 * 2. The Server receives them and converts it to a User class object.
 * Then it generates a JSON web token using secret
 * 3. JWT.create() generates a token with the specified JWT settings,
 * adds a custom claim with a received username, and signs a token with the specified algorithm:
 *
 * For HS256, a shared secret is used to sign a token.
 *
 * For RS256, a public/private key pair is used.
 *
 * 4. The server sends a token to a client as a JSON object.
 *
 * 5. Whenever the logged user wants to reach a secured resource, he/she provides the jwt to the server
 *
 * See: https://auth0.com/learn/json-web-tokens
 */

/**
 * @TODO: how do i test this one??
 */
@Service
class JwtServiceImpl : JwtService {

    private val secret = "secret"

    /**
     * Extract a specific claim using Getter of Claims interface.
     */
    override fun extractClaims(token: String, claimsResolver: Function<Claims, Any>): Any {
        return claimsResolver.apply(extractAllClaims(token));
    }

    /**
     * Reading Claims from Token (username, etc.): Deserialization.
     *
     */
    override fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).body
    }

    override fun validateToken(token: String, userDetails: UserDetails): Boolean {
        return extractUserName(token) == userDetails.username && !isTokenExpired(token)
    }

    override fun generateToken(email: String, role: String): String {
        val claims = hashMapOf("role" to role)
        return createToken(claims, email)
    }

    override fun extractUserName(token: String): String? {
        return extractClaims(token, Claims::getSubject) as String
    }

    override fun extractExpiration(token: String): Date? {
        return extractClaims(token, Claims::getExpiration) as Date
    }

    /**
     * Creating a new token for 10-hours duration
     */
    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .setSubject(subject)
            .setClaims(claims)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }

    private fun isTokenExpired(token: String): Boolean {
        return if (extractExpiration(token) == null) false else extractExpiration(token)!!.before(
            Date.from(
                LocalDate.now().atStartOfDay(
                    ZoneId.systemDefault()
                ).toInstant()
            )
        )
    }
}