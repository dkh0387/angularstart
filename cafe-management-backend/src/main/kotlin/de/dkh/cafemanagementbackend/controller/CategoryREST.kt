package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.wrapper.CategoryWrapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/category")
interface CategoryREST {

    @PostMapping("/add")
    fun addCategory(@RequestBody(required = true) requestMap: Map<String, String>): ResponseEntity<String>

    @GetMapping("/get")
    fun getAllCategory(@RequestParam(required = false) filterValue: String?): ResponseEntity<List<CategoryWrapper>>

    @PostMapping("/update")
    fun updateCategory(@RequestBody(required = true) requestMap: Map<String, String>): ResponseEntity<String>

    @PostMapping("/delete/{id}")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<String>
}
