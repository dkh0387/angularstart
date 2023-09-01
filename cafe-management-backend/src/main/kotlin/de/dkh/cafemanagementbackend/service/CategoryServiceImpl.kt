package de.dkh.cafemanagementbackend.service

import com.google.common.base.Strings
import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.Category
import de.dkh.cafemanagementbackend.exception.AddCategoryException
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.repository.CategoryRepository
import de.dkh.cafemanagementbackend.utils.CafeUtils
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import de.dkh.cafemanagementbackend.utils.mapper.CategoryMapper
import de.dkh.cafemanagementbackend.wrapper.CategoryWrapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(private val categoryRepository: CategoryRepository, private val jwtFilter: JwtFilter) :
    CategoryService {

    private val logger: Logger = LoggerFactory.getLogger(CategoryServiceImpl::class.java)

    /**
     *Adding a new product category. Requires admin access.
     */
    override fun addCategory(requestMap: Map<String, String>): ResponseEntity<String> {
        println("Inside addCategory $requestMap")

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

    override fun getAllCategory(filterValue: String?): ResponseEntity<List<CategoryWrapper>> {
        println("Inside getAllCategory $filterValue")

        try {
            return if (!Strings.isNullOrEmpty(filterValue) && filterValue.equals(CafeConstants.TRUE, true)) {
                CafeUtils.getCategoryResponseFor(
                    categoryRepository.getAllCategory().map { it.toWrapper() },
                    HttpStatus.OK
                )
            } else {
                CafeUtils.getCategoryResponseFor(
                    categoryRepository.findAll().map { it.toWrapper() },
                    HttpStatus.OK
                )
            }

        } catch (e: Exception) {
            return CafeUtils.getCategoryResponseFor(
                emptyList(),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}