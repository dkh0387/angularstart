package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.exception.*
import de.dkh.cafemanagementbackend.service.ProductService
import de.dkh.cafemanagementbackend.wrapper.ProductWrapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductRESTImpl(private val productService: ProductService) : ProductREST {

    @Throws(AddProductException::class)
    override fun addProduct(requestMap: Map<String, String>): ResponseEntity<String> {
        return productService.addProduct(requestMap)
    }

    @Throws(GetAllProductException::class)
    override fun getAllProduct(): ResponseEntity<List<ProductWrapper>> {
        return productService.getAllProduct()
    }

    @Throws(UpdateProductException::class)
    override fun updateProduct(requestMap: Map<String, String>): ResponseEntity<String> {
        return productService.updateProduct(requestMap)
    }

    @Throws(DeleteProductException::class)
    override fun deleteProductWithRequestParam(id: String): ResponseEntity<String> {
        return productService.deleteProduct(Integer.parseInt(id).toLong())
    }

    @Throws(DeleteProductException::class)
    override fun deleteProductWithPathVariable(id: Long): ResponseEntity<String> {
        return productService.deleteProduct(id)
    }

    @Throws(UpdateProductStatusException::class)
    override fun updateProductStatus(requestMap: Map<String, String>): ResponseEntity<String> {
        return productService.updateProductStatus(requestMap)
    }

    @Throws(GetAllProductByCategoryException::class)
    override fun getProductsByCategory(categoryId: Long): ResponseEntity<List<ProductWrapper>> {
        return productService.getProductsByCategory(categoryId)
    }


}