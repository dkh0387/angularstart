package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.Product
import de.dkh.cafemanagementbackend.exception.AddProductException
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.repository.CategoryRepository
import de.dkh.cafemanagementbackend.repository.ProductRepository
import de.dkh.cafemanagementbackend.utils.CafeUtils
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import de.dkh.cafemanagementbackend.utils.mapper.ProductMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val jwtFilter: JwtFilter
) :
    ProductService {

    private val logger: Logger = LoggerFactory.getLogger(ProductServiceImpl::class.java)

    override fun addProduct(requestMap: Map<String, String>): ResponseEntity<String> {
        try {
            return if (jwtFilter.isAdmin()) {
                val productMapper =
                    ServiceUtils.getMapperFromRequestMap(requestMap, ProductMapper::class.java) as ProductMapper
                productMapper.categoryRepository = categoryRepository

                val product = Product.createFromMapper(productMapper)
                productRepository.save(product)
                CafeUtils.getStringResponseFor(CafeConstants.ADD_PRODUCT_SUCCESSFULLY, HttpStatus.OK)
            } else {
                CafeUtils.getStringResponseFor(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED)
            }

        } catch (e: Exception) {
            logger.error(CafeConstants.ADD_PRODUCT_WENTWRONG + " MESSAGE: + ${e.localizedMessage}")
            throw AddProductException(
                CafeConstants.ADD_PRODUCT_WENTWRONG + " MESSAGE: + ${e.localizedMessage}",
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}