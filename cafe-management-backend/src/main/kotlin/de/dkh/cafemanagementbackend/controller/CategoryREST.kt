package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.entity.Category
import de.dkh.cafemanagementbackend.wrapper.CategoryWrapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/category")
interface CategoryREST {

    @PostMapping("/add")
    fun addCategory(@RequestBody(required = true) requestMap: Map<String, String>): ResponseEntity<String>

    @GetMapping("/get")
    fun getAllCategory(@RequestParam(required = false) filterValue: String?): ResponseEntity<List<CategoryWrapper>>

}
