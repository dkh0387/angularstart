package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.wrapper.ProductWrapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/product"])
interface ProductREST {

    @PostMapping("/add")
    fun addProduct(@RequestBody(required = true) requestMap: Map<String, String>): ResponseEntity<String>

    @GetMapping("/get")
    fun getAllProduct(): ResponseEntity<List<ProductWrapper>>

    @PostMapping("/update")
    fun updateProduct(@RequestBody(required = true) requestMap: Map<String, String>): ResponseEntity<String>

    /**
     * 1. Way to use parametrized URL: http://localhost:8081/product/delete/?id=52
     */
    @PostMapping("/delete/")
    fun deleteProductWithRequestParam(@RequestParam id: String): ResponseEntity<String>

    /**
     * 2. Way to use parametrized URL: http://localhost:8081/product/delete/52
     */
    @PostMapping("/delete/{id}")
    fun deleteProductWithPathVariable(@PathVariable id: Long): ResponseEntity<String>

    @PostMapping("/updateStatus")
    fun updateProductStatus(@RequestBody(required = true) requestMap: Map<String, String>): ResponseEntity<String>

    @GetMapping("/getByCategory/{categoryId}")
    fun getProductsByCategory(@PathVariable categoryId: Long): ResponseEntity<List<ProductWrapper>>

    @GetMapping("/getById/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<ProductWrapper>
}