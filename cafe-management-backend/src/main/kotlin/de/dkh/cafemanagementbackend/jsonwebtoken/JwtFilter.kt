package de.dkh.cafemanagementbackend.jsonwebtoken

import de.dkh.cafemanagementbackend.service.CustomerUserDetailsService
import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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

/**
 * @TODO: how do i test this one??
 */
@Component
class JwtFilter(
    private val jwtService: JwtService,
    private val customerUserDetailsService: CustomerUserDetailsService
) : OncePerRequestFilter() {

    private lateinit var claims: Claims
    private var userName: String? = null
    override fun doFilterInternal(
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
            if (userName != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails = customerUserDetailsService.loadUserByUsername(userName)

                if (jwtService.validateToken(token, userDetails)) {
                    val usernamePasswordAuthenticationToken =
                        UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
                }
            }
            filterChain.doFilter(request, response)
        }
    }

    private fun extractToken(header: String): String {
        val token = header.substring(7)

        if (header != null && header.startsWith("Bearer ")) {
            userName = jwtService.extractUserName(token)
            claims = jwtService.extractAllClaims(token)
        }
        return token
    }
}