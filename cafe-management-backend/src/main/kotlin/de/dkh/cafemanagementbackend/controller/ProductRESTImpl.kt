package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.exception.AddProductException
import de.dkh.cafemanagementbackend.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductRESTImpl(private val productService: ProductService) : ProductREST {

    @Throws(AddProductException::class)
    override fun addProduct(requestMap: Map<String, String>): ResponseEntity<String> {
        return productService.addProduct(requestMap)
    }
}