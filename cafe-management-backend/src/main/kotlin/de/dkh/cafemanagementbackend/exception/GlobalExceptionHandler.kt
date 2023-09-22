package de.dkh.cafemanagementbackend.exception

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.utils.CafeUtils
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(AddCategoryException::class)
    fun handleAddCategoryException(ex: AddCategoryException): ResponseEntity<String> {
        return CafeUtils.getStringResponseFor(
            if (ex.message == null) CafeConstants.ADD_CATEGORY_WENT_WRONG else ex.message!!, ex.httpStatus
        )
    }

    @ExceptionHandler(AddProductException::class)
    fun handleAddProductException(ex: AddProductException): ResponseEntity<String> {
        return CafeUtils.getStringResponseFor(
            if (ex.message == null) CafeConstants.ADD_PRODUCT_WENT_WRONG else ex.message!!, ex.httpStatus
        )
    }
}