package de.dkh.cafemanagementbackend.repository

import de.dkh.cafemanagementbackend.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(@Param("emailInput") email: String): User
}