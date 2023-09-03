package de.dkh.cafemanagementbackend.repository

import de.dkh.cafemanagementbackend.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {

    fun deleteByName(name: String)
}