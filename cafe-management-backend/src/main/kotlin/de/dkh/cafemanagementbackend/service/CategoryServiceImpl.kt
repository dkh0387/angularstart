package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.Category
import de.dkh.cafemanagementbackend.exception.AddCategoryException
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.repository.CategoryRepository
import de.dkh.cafemanagementbackend.utils.CafeUtils
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import de.dkh.cafemanagementbackend.utils.mapper.CategoryMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(private val categoryRepository: CategoryRepository, private val jwtFilter: JwtFilter) :
    CategoryService {
    /**
     *Adding a new product category. Requires admin access.
     */
    override fun addCategory(requestMap: Map<String, String>): ResponseEntity<String> {
        try {
            return if (jwtFilter.isAdmin()) {
                val categoryMapper =
                    ServiceUtils.getMapperFromRequestMap(requestMap, CategoryMapper::class.java) as CategoryMapper
                val category = Category.createFromMapper(categoryMapper)
                categoryRepository.save(category)
                CafeUtils.getStringResponseFor(CafeConstants.ADD_CATEGORY_SUCCESSFULLY, HttpStatus.OK)
            } else {
                CafeUtils.getStringResponseFor(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED)
            }

        } catch (e: Exception) {
            throw AddCategoryException(
                CafeConstants.ADD_CATEGORY_WENT_WRONG + " MESSAGE: " + e.localizedMessage,
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}