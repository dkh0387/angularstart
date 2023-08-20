package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.repository.UserRepository
import lombok.Getter
import lombok.extern.slf4j.Slf4j
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.Objects

/**
 * Implementation of the UserDetailsService, using by Spring security as default.
 * This one will be provided to the AuthenticationManager within [de.dkh.cafemanagementbackend.config.SecurityConfig].
 */

/**
 * @TODO: testing!
 */
@Service
@Slf4j
class CustomerUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    @Getter
    private lateinit var userDetail: de.dkh.cafemanagementbackend.entity.User

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String?): UserDetails {
        println("Inside loadUserByUsername with email: $email")

        val userDetailFromDB = userRepository.findByEmail(email)

        if (Objects.isNull(userDetailFromDB)) {
            throw UsernameNotFoundException(CafeConstants.USER_COULD_NOT_FOUND_BY_EMAIL)
        }
        userDetail = userDetailFromDB!!
        return User(userDetail.email, userDetail.password, ArrayList())
    }
}