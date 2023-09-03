package de.dkh.cafemanagementbackend.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping(path = ["/product"])
interface ProductREST {

    @PostMapping("/add")
    fun addProduct(@RequestBody(required = true) requestMap: Map<String, String>): ResponseEntity<String>
}