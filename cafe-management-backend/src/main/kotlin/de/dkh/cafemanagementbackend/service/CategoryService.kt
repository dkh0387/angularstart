package de.dkh.cafemanagementbackend.service

import org.springframework.http.ResponseEntity

interface CategoryService {
    fun addCategory(requestMap: Map<String, String>): ResponseEntity<String>
}