package de.dkh.cafemanagementbackend.repository

import de.dkh.cafemanagementbackend.entity.Authority
import org.springframework.data.jpa.repository.JpaRepository

interface AuthorityRepository : JpaRepository<Authority, Long> {
}