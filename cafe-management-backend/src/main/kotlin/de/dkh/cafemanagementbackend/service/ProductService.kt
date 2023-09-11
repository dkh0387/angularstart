package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.wrapper.ProductWrapper
import org.springframework.http.ResponseEntity

interface ProductService {
    fun addProduct(requestMap: Map<String, String>): ResponseEntity<String>
    fun getAllProduct(): ResponseEntity<List<ProductWrapper>>
    fun updateProduct(requestMap: Map<String, String>): ResponseEntity<String>
    fun deleteProduct(id: Long): ResponseEntity<String>


}