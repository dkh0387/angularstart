package de.dkh.cafemanagementbackend.repository

import org.springframework.data.jpa.repository.JpaRepository
import de.dkh.cafemanagementbackend.entity.User

interface UserRepository : JpaRepository<User, Long> {
}