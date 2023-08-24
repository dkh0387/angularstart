package de.dkh.cafemanagementbackend.jsonwebtoken

import de.dkh.cafemanagementbackend.service.CustomerUserDetailsService
import de.dkh.cafemanagementbackend.testutils.TestData
import io.jsonwebtoken.impl.DefaultClaims
import io.mockk.*
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

class JwtFilterTest {

    private var request = spyk<HttpServletRequest>()
    private var response = mockk<HttpServletResponse>()
    private var filterChain = mockk<FilterChain>()

    private val jwtService = mockk<JwtService>()
    private val customerUserDetailsService = mockk<CustomerUserDetailsService>()

    private val objectUnderTest = JwtFilter(jwtService, customerUserDetailsService)

    @Nested
    @DisplayName("JwtFilter Testing")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class TestingJwtFilter {

        @Test
        fun `should call doFilter() if the request servletPath matches`() {
            // given
            every { request.servletPath } returns "/user/login"
            every { filterChain.doFilter(request, response) } just runs

            // when
            objectUnderTest.doFilterInternal(request, response, filterChain)

            // then
            verify(exactly = 1)
            { filterChain.doFilter(request, response) }
        }

        @Test
        fun `should call doFilter() immediately if the token is not valid`() {
            // given
            every { request.servletPath } returns "/user/login2"
            every { request.getHeader("Authorization") } returns "Bearer S0VLU0UhIExFQ0tFUiEK"
            every { jwtService.validateToken(any(), any()) } returns false
            every { jwtService.extractUserName(any()) } returns "deniskh87@gmail.com"
            every { jwtService.extractAllClaims(any()) } returns DefaultClaims(mapOf("role" to "user"))
            every { customerUserDetailsService.loadUserByUsername(any()) } returns TestData.getSpringUserDetails()
            every { filterChain.doFilter(request, response) } just runs

            // when
            objectUnderTest.doFilterInternal(request, response, filterChain)

            // then
            verify(exactly = 1)
            { filterChain.doFilter(request, response) }
        }

        @Test
        fun `should set authentication on SecurityContextHolder and call doFilter() if the token is valid`() {
            // given
            every { request.servletPath } returns "/user/login2"
            every { request.getHeader("Authorization") } returns "Bearer S0VLU0UhIExFQ0tFUiEK"
            every { jwtService.validateToken(any(), any()) } returns true
            every { jwtService.extractUserName(any()) } returns "deniskh87@gmail.com"
            every { jwtService.extractAllClaims(any()) } returns DefaultClaims(mapOf("role" to "user"))
            every { customerUserDetailsService.loadUserByUsername(any()) } returns TestData.getSpringUserDetails()
            every { filterChain.doFilter(request, response) } just runs

            // when
            objectUnderTest.doFilterInternal(request, response, filterChain)

            // then
            assertThat(objectUnderTest.securityContext.authentication).isNotNull

            verify(exactly = 1) {
                filterChain.doFilter(
                    request,
                    response
                )
            }
        }

    }
}