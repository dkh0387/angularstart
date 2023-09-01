package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.service.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class CategoryRESTImpl(private val categoryService: CategoryService) : CategoryREST {
    override fun addCategory(requestMap: Map<String, String>): ResponseEntity<String> {
        return categoryService.addCategory(requestMap)
    }
}