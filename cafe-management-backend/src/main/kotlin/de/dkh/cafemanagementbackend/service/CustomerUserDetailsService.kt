package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.repository.UserRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList


/**
 * Implementation of the UserDetailsService, using by Spring security as default.
 * This one will be provided to the AuthenticationManager within [de.dkh.cafemanagementbackend.config.SecurityConfig].
 */
@Service
@Slf4j
class CustomerUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    private lateinit var userDetail: de.dkh.cafemanagementbackend.entity.User

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String?): UserDetails {
        println("Inside loadUserByUsername with email: $email")

        val userDetailFromDB = userRepository.findByEmail(email)

        if (Objects.isNull(userDetailFromDB)) {
            throw UsernameNotFoundException(CafeConstants.USER_COULD_NOT_FOUND_BY_EMAIL)
        }
        userDetail = userDetailFromDB!!
        val authorities = ArrayList<GrantedAuthority?>()

        authorities.add(SimpleGrantedAuthority(userDetail.role))

        return User(
            userDetail.email,
            userDetail.password,
            authorities
        )
    }

    fun getUserDetailWithoutPassword(): de.dkh.cafemanagementbackend.entity.User = userDetail.copy(password = null)

    fun checkUserApproved(): Boolean = getUserDetailWithoutPassword().status.equals("true", true)

}