package de.dkh.cafemanagementbackend.config

import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.service.CustomerUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


/**
 * Spring security configuration.
 * NOTE: we are on Spring boot 3 here, so we can not use the deprecated [WebSecurityConfigurerAdapter]!
 * For details see: https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customerUserDetailsService: CustomerUserDetailsService,
    private val jwtFilter: JwtFilter
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
        http
            .authorizeHttpRequests { authorizeHttpRequests ->
                authorizeHttpRequests
                    //.requestMatchers("/**").hasRole("USER")
                    .requestMatchers("user/login", "user/signup", "user/forgotPassword").permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .csrf { csrf -> csrf.disable() }
            .exceptionHandling { exceptionHandling ->
                exceptionHandling
                    .accessDeniedPage("/errors/access-denied")
            }
            .sessionManagement { sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        //.formLogin(withDefaults())

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }


}