package de.dkh.cafemanagementbackend.config

import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.service.CustomerUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.core.GrantedAuthorityDefaults
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.util.*


/**
 * Spring security configuration.
 * NOTE: we are on Spring boot 3 here, so we can not use the deprecated [WebSecurityConfigurerAdapter]!
 * For details see: https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter.
 * @EnableMethodSecurity allowes to use annotion based configuration of role each endpoint, see [de.dkh.cafemanagementbackend.controller.UserREST].
 */
@Configuration
@EnableWebSecurity/*@EnableMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)*/
class SecurityConfig(
    private val customerUserDetailsService: CustomerUserDetailsService, private val jwtFilter: JwtFilter
) {
    /**
     * In the old version, you inject AuthenticationManagerBuilder, set userDetailsService, passwordEncoder, and build it.
     * But authenticationManager is already created in this step. It is created the way we wanted (with userDetailsService and the passwordEncoder).
     */
    @Bean(name = [BeanIds.AUTHENTICATION_MANAGER])
    @Throws(Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.getAuthenticationManager()
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(customerUserDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }

    /**
     * The most basic example is to configure all URLs to require the role "ROLE_USER".
     * The configuration below requires authentication to every URL and will grant access to both the user "admin" and "user".
     * Further, there is no authentication required for landing pages like login or forgetPassword.
     * All other requests require authentication.
     * CSFR is disabled.
     * Spring Security will never create an HttpSession, and it will never use it to obtain the SecurityContext
     *
     */
    @Bean
    @Throws(java.lang.Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { authorizeHttpRequests ->
            authorizeHttpRequests
                //.requestMatchers("/**").hasAuthority("ROLE_USER")
                .requestMatchers(
                    "user/login", "user/signup", "user/forgotPassword", "user/changePassword"
                ).permitAll().requestMatchers(
                    "product/getByCategory/*", "product/getById/*", "bill/generate"
                ).hasAuthority("ROLE_USER").requestMatchers(
                    "user/get",
                    "user/update",
                    "category/add",
                    "category/get",
                    "category/update",
                    "product/add",
                    "product/get",
                    "product/update",
                    "product/delete/*"
                ).hasAuthority("ROLE_ADMIN").requestMatchers(
                    "bill/get", "bill/getBillDocument", "bill/delete/*", "dashboard/details"
                ).hasAnyAuthority("ROLE_ADMIN", "ROLE_USER").requestMatchers("user/checkToken")
                .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER").anyRequest().authenticated()
        }.csrf { csrf -> csrf.disable() }.cors { cors -> cors.configurationSource(urlBasedCorsConfigurationSource()) }
            .exceptionHandling { exceptionHandling ->
                exceptionHandling.accessDeniedPage("/errors/access-denied")
            }
            .sessionManagement { sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .logout { lo -> lo.permitAll() }
        //.formLogin(withDefaults())

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    /**
     * Used by Spring Security if CORS is enabled.
     * This one is required if we want to call endpoint from frontend (localhost:4200).
     */
    @Bean
    fun corsFilter(): CorsFilter {
        return CorsFilter(urlBasedCorsConfigurationSource())
    }

    @Bean
    fun grantedAuthorityDefaults(): GrantedAuthorityDefaults {
        return GrantedAuthorityDefaults("") // Remove the ROLE_ prefix
    }

    /**
     * @TODO: change if full app is ready!
     * CORS configuration.
     * See https://medium.com/techpanel/guide-to-cors-in-spring-boot-e76d317b9b36 for details.
     */
    private fun urlBasedCorsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOriginPattern("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return source
    }

}