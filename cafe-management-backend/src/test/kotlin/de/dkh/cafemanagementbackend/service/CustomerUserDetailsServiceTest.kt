package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.repository.UserRepository
import de.dkh.cafemanagementbackend.testutils.TestData
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.security.core.userdetails.UsernameNotFoundException

class CustomerUserDetailsServiceTest {

    private val userRepository = mockk<UserRepository>()
    private val objectUnderTest: CustomerUserDetailsService = CustomerUserDetailsService(userRepository)

    @Nested
    @DisplayName("Testing laoding user by email")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class LoadUserByEmailTesting {

        @Test
        fun `should throw a UsernameNotFoundException if there is no user for the given email in the database`() {
            // given
            every { userRepository.findByEmail(any()) } returns null

            // when / then
            assertThrows<UsernameNotFoundException> { objectUnderTest.loadUserByUsername("deniskh87@gmail.com") }

        }

        @Test
        fun `should return valid user if there is a user for the given email in the database`() {
            // given
            every { userRepository.findByEmail(any()) } returns TestData.getInactiveUserWithAuthorities()

            // when
            val user = objectUnderTest.loadUserByUsername("deniskh87@gmail.com")

            // then
            assertThat(user).isNotNull
            assertThat(user.username).isEqualTo("deniskh87@gmail.com")
        }

    }

}