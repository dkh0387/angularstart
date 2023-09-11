package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.wrapper.ProductWrapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping(path = ["/product"])
interface ProductREST {

    @PostMapping("/add")
    fun addProduct(@RequestBody(required = true) requestMap: Map<String, String>): ResponseEntity<String>

    @GetMapping("/get")
    fun getAllProduct(): ResponseEntity<List<ProductWrapper>>

    @PostMapping("/update")
    fun updateProduct(@RequestBody(required = true) requestMap: Map<String, String>): ResponseEntity<String>
}