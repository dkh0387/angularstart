package de.dkh.cafemanagementbackend.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/category")
interface CategoryREST {

    @PostMapping("/add")
    fun addCategory(@RequestBody(required = true) requestMap: Map<String, String>): ResponseEntity<String>

}
