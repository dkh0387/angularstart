package de.dkh.cafemanagementbackend.repository

import de.dkh.cafemanagementbackend.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.transaction.annotation.Transactional

interface CategoryRepository : JpaRepository<Category, Long> {

    fun findAllForProduct(): List<Category>

    @Transactional
    @Modifying
    fun deleteByName(name: String)
}