package de.dkh.cafemanagementbackend.repository

import de.dkh.cafemanagementbackend.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.transaction.annotation.Transactional

interface ProductRepository : JpaRepository<Product, Long> {

    @Transactional
    @Modifying
    fun deleteByName(name: String)
}