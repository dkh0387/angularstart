package de.dkh.cafemanagementbackend.repository

import de.dkh.cafemanagementbackend.entity.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long> {

    fun findAllForProduct(): List<Category>
}