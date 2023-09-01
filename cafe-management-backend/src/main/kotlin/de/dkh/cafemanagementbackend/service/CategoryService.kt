package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.wrapper.CategoryWrapper
import org.springframework.http.ResponseEntity

interface CategoryService {
    fun addCategory(requestMap: Map<String, String>): ResponseEntity<String>
    fun getAllCategory(filterValue: String?): ResponseEntity<List<CategoryWrapper>>
    fun updateCategory(requestMap: Map<String, String>): ResponseEntity<String>
}