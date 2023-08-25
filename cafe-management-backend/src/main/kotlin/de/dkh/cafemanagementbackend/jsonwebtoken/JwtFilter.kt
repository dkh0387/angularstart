package de.dkh.cafemanagementbackend.jsonwebtoken

import de.dkh.cafemanagementbackend.service.CustomerUserDetailsService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.impl.DefaultClaims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.Getter
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Validation of JSON web token.
 * If a user is signing up or goes for forget password, we do not apply a filter,
 * otherwise we do the validation of the provided token and then pass the user through the filter.
 */
@Component
class JwtFilter(
    private val jwtService: JwtService,
    private val customerUserDetailsService: CustomerUserDetailsService
) : OncePerRequestFilter() {

    var securityContext = SecurityContextHolder.getContext()
    var claims: Claims = DefaultClaims()

    @Getter
    private var userName: String? = null

    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.servletPath.matches(Regex("/user/login|/user/signup|/user/forgotPassword"))) {
            filterChain.doFilter(request, response)
        } else {
            val header = request.getHeader("Authorization")
            // Extracting token claims using the service
            val token: String = extractToken(header)
            // Check the user credentials and validate the token
            if (userName != null && securityContext.authentication == null) {
                val userDetails = customerUserDetailsService.loadUserByUsername(userName)

                if (jwtService.validateToken(token, userDetails)) {
                    val usernamePasswordAuthenticationToken =
                        UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    securityContext.authentication = usernamePasswordAuthenticationToken
                }
            }
            filterChain.doFilter(request, response)
        }
    }

    fun isAdmin(): Boolean = hasRole("admin")

    fun isUser(): Boolean = hasRole("user")

    private fun hasRole(role: String): Boolean = role.equals(claims["role"] as String?, true)

    private fun extractToken(header: String): String {
        val token = header.substring(7)

        if (header.startsWith("Bearer ")) {
            userName = jwtService.extractUserName(token)
            claims = jwtService.extractAllClaims(token)
        }
        return token
    }
}