package de.dkh.cafemanagementbackend.service

import org.springframework.http.ResponseEntity

interface ProductService {
    fun addProduct(requestMap: Map<String, String>): ResponseEntity<String>
}