package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.exception.AddCategoryException
import de.dkh.cafemanagementbackend.exception.DeleteCategoryException
import de.dkh.cafemanagementbackend.exception.GetAllCategoryException
import de.dkh.cafemanagementbackend.exception.UpdateCategoryException
import de.dkh.cafemanagementbackend.service.CategoryService
import de.dkh.cafemanagementbackend.wrapper.CategoryWrapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CategoryRESTImpl(private val categoryService: CategoryService) : CategoryREST {
    @Throws(AddCategoryException::class)
    override fun addCategory(requestMap: Map<String, String>): ResponseEntity<String> {
        return categoryService.addCategory(requestMap)
    }

    @Throws(GetAllCategoryException::class)
    override fun getAllCategory(filterValue: String?): ResponseEntity<List<CategoryWrapper>> {
        return categoryService.getAllCategory(filterValue)
    }

    @Throws(UpdateCategoryException::class)
    override fun updateCategory(requestMap: Map<String, String>): ResponseEntity<String> {
        return categoryService.updateCategory(requestMap)
    }

    /**
     * TODO: testing
     */
    @Throws(DeleteCategoryException::class)
    override fun deleteCategory(@PathVariable id: Long): ResponseEntity<String> {
        return categoryService.deleteCategory(id)
    }
}