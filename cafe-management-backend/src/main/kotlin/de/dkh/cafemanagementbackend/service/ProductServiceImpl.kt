package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.Product
import de.dkh.cafemanagementbackend.exception.*
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.repository.CategoryRepository
import de.dkh.cafemanagementbackend.repository.ProductRepository
import de.dkh.cafemanagementbackend.utils.CafeUtils
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import de.dkh.cafemanagementbackend.utils.mapper.ProductMapper
import de.dkh.cafemanagementbackend.wrapper.ProductWrapper
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
) : ProductService, LoggerService {

    private val logger: Logger = LoggerFactory.getLogger(ProductServiceImpl::class.java)

    override fun addProduct(requestMap: Map<String, String>): ResponseEntity<String> {
        try {
            return if (jwtFilter.isAdmin()) {
                val productMapper =
                    ServiceUtils.getMapperFromRequestStringMap(requestMap, ProductMapper::class.java) as ProductMapper
                productMapper.categoryRepository = categoryRepository

                val product = Product.createFromMapper(productMapper)
                productRepository.save(product)
                CafeUtils.getStringResponseFor(CafeConstants.ADD_PRODUCT_SUCCESSFULLY, HttpStatus.OK)
            } else {
                CafeUtils.getStringResponseFor(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED)
            }

        } catch (e: Exception) {
            logAndThrow(
                logger, CafeConstants.ADD_PRODUCT_WENT_WRONG, e, AddProductException(
                    CafeConstants.ADD_PRODUCT_WENT_WRONG + " MESSAGE: + ${e.localizedMessage}",
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
        }
        return CafeUtils.getStringResponseFor(CafeConstants.ADD_PRODUCT_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    override fun getAllProduct(): ResponseEntity<List<ProductWrapper>> {
        try {
            return if (jwtFilter.isAdmin()) {
                CafeUtils.getProductResponseFor(
                    productRepository.getAllProduct(), HttpStatus.OK
                )
            } else {
                CafeUtils.getProductResponseFor(emptyList(), HttpStatus.UNAUTHORIZED)
            }
        } catch (e: Exception) {
            logAndThrow(
                logger, CafeConstants.GET_ALL_PRODUCT_WENT_WRONG, e, GetAllProductException(
                    CafeConstants.GET_ALL_PRODUCT_WENT_WRONG + " MESSAGE: + ${e.localizedMessage}",
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
        }
        return CafeUtils.getProductResponseFor(emptyList(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    override fun updateProduct(requestMap: Map<String, String>): ResponseEntity<String> {
        println("Inside updateProduct $requestMap")

        try {
            val productMapper =
                ServiceUtils.getMapperFromRequestStringMap(requestMap, ProductMapper::class.java) as ProductMapper
            productMapper.categoryRepository = categoryRepository
            val productOptional = productMapper.id?.let { productRepository.findById(it) }

            return if (jwtFilter.isAdmin()) {
                if (productOptional != null && productOptional.isPresent) {
                    val product = productOptional.get()
                    product.name = productMapper.name
                    product.description = productMapper.description
                    product.price = productMapper.price
                    product.category = productMapper.getCategory().get()
                    product.status = productMapper.status!!
                    productRepository.save(product)
                    CafeUtils.getStringResponseFor(CafeConstants.UPDATE_PRODUCT_SUCCESSFULLY, HttpStatus.OK)
                } else {
                    CafeUtils.getStringResponseFor(
                        CafeConstants.UPDATE_PRODUCT_WENT_WRONG, HttpStatus.BAD_REQUEST
                    )
                }
            } else {
                CafeUtils.getStringResponseFor(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED)
            }
        } catch (e: Exception) {
            logAndThrow(
                logger, CafeConstants.UPDATE_PRODUCT_WENT_WRONG, e, UpdateProductException(
                    CafeConstants.UPDATE_PRODUCT_WENT_WRONG + " MESSAGE: + ${e.localizedMessage}",
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
        }
        return CafeUtils.getStringResponseFor(CafeConstants.UPDATE_PRODUCT_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    override fun deleteProduct(id: Long): ResponseEntity<String> {
        println("Inside deleteProduct $id")

        try {
            return if (jwtFilter.isAdmin()) {
                val productOptional = productRepository.findById(id)

                if (productOptional.isPresent) {
                    productRepository.deleteById(productOptional.get().id)
                    CafeUtils.getStringResponseFor(CafeConstants.DELETE_PRODUCT_SUCCESSFULLY, HttpStatus.OK)
                } else {
                    CafeUtils.getStringResponseFor(
                        CafeConstants.DELETE_PRODUCT_WENT_WRONG,
                        HttpStatus.BAD_REQUEST
                    )
                }
            } else {
                CafeUtils.getStringResponseFor(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED)
            }
        } catch (e: Exception) {
            logAndThrow(
                logger, CafeConstants.DELETE_PRODUCT_WENT_WRONG, e, DeleteProductException(
                    CafeConstants.DELETE_PRODUCT_WENT_WRONG + " MESSAGE: + ${e.localizedMessage}",
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
        }
        return CafeUtils.getStringResponseFor(CafeConstants.DELETE_PRODUCT_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    override fun updateProductStatus(requestMap: Map<String, String>): ResponseEntity<String> {
        println("Inside updateProductStatus $requestMap")

        try {
            val productMapper =
                ServiceUtils.getMapperFromRequestStringMap(requestMap, ProductMapper::class.java) as ProductMapper
            productMapper.categoryRepository = categoryRepository
            val productOptional = productMapper.id?.let { productRepository.findById(it) }

            return if (jwtFilter.isAdmin()) {
                if (productOptional != null && productOptional.isPresent) {
                    val product = productOptional.get()
                    productRepository.updateStatus(productMapper.status!!, product.id)
                    CafeUtils.getStringResponseFor(CafeConstants.UPDATE_PRODUCT_STATUS_SUCCESSFULLY, HttpStatus.OK)
                } else {
                    CafeUtils.getStringResponseFor(
                        CafeConstants.UPDATE_PRODUCT_STATUS_WENT_WRONG, HttpStatus.BAD_REQUEST
                    )
                }
            } else {
                CafeUtils.getStringResponseFor(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED)
            }
        } catch (e: Exception) {
            logAndThrow(
                logger, CafeConstants.UPDATE_PRODUCT_STATUS_WENT_WRONG, e, UpdateProductStatusException(
                    CafeConstants.UPDATE_PRODUCT_STATUS_WENT_WRONG + " MESSAGE: + ${e.localizedMessage}",
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
        }
        return CafeUtils.getStringResponseFor(
            CafeConstants.UPDATE_PRODUCT_STATUS_WENT_WRONG,
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    override fun getProductsByCategory(categoryId: Long): ResponseEntity<List<ProductWrapper>> {
        println("Inside getProductsByCategory $categoryId")

        try {
            val categoryOptional = categoryRepository.findById(categoryId)

            return if (categoryOptional.isPresent) {
                val products = productRepository.findByCategoryAndStatus(categoryOptional.get(), CafeConstants.TRUE)
                CafeUtils.getProductResponseFor(products, HttpStatus.OK)
            } else {
                CafeUtils.getProductResponseFor(emptyList(), HttpStatus.BAD_REQUEST)
            }
        } catch (e: Exception) {
            logAndThrow(
                logger,
                CafeConstants.GET_ALL_PRODUCT_BY_CATEGORY_STATUS_WENT_WRONG,
                e,
                GetAllProductByCategoryException(
                    CafeConstants.GET_ALL_PRODUCT_BY_CATEGORY_STATUS_WENT_WRONG + " MESSAGE: + ${e.localizedMessage}",
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
        }
        return CafeUtils.getProductResponseFor(emptyList(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    override fun getProductById(id: Long): ResponseEntity<ProductWrapper> {
        println("Inside getProductById $id")

        try {
            val productOptional = productRepository.findByIdAndStatus(id, CafeConstants.TRUE)

            return if (productOptional.isPresent) {
                CafeUtils.getSingleProductResponseFor(productOptional.get().toWrapper(), HttpStatus.OK)
            } else {
                CafeUtils.getSingleProductResponseFor(null, HttpStatus.BAD_REQUEST)
            }
        } catch (e: Exception) {
            logAndThrow(
                logger, CafeConstants.GET_PRODUCT_BY_ID_WENT_WRONG, e, GetProductByIdException(
                    CafeConstants.GET_PRODUCT_BY_ID_WENT_WRONG + " MESSAGE: + ${e.localizedMessage}",
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
        }
        return CafeUtils.getSingleProductResponseFor(null, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}