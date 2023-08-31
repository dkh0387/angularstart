package de.dkh.cafemanagementbackend.jsonwebtoken

import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.service.CustomerUserDetailsService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.impl.DefaultClaims
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.Getter
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


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
    private var currentUser: UserDetails? = null

    @Throws(IOException::class, ServletException::class)
    public override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.servletPath.matches(Regex("/user/login|/user/signup|/user/forgotPassword"))) {
            filterChain.doFilter(request, response)
            return
        }
        val header = request.getHeader("Authorization")
        val token: String = extractToken(header)
        val username: String? = jwtService.extractUserName(token)

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails: UserDetails = customerUserDetailsService.loadUserByUsername(username)
            val isValidToken: Boolean = jwtService.validateToken(token, userDetails)

            if (isValidToken) {
                currentUser = userDetails
                val usernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken(currentUser, null, userDetails.authorities)
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                securityContext.authentication = usernamePasswordAuthenticationToken
                SecurityContextHolder.setContext(securityContext)
            }
        }
        filterChain.doFilter(request, response)
    }

    fun getCurrentUser(): UserDetails? = currentUser

    fun setCurrentUser(userDetails: UserDetails?) {
        currentUser = userDetails
    }

    fun isAdmin(): Boolean = hasRole(User.UserRoles.ROLE_ADMIN.name)

    fun isUser(): Boolean = hasRole(User.UserRoles.ROLE_USER.name)

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