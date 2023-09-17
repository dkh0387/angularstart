package de.dkh.cafemanagementbackend.utils

import de.dkh.cafemanagementbackend.wrapper.BillWrapper
import de.dkh.cafemanagementbackend.wrapper.CategoryWrapper
import de.dkh.cafemanagementbackend.wrapper.ProductWrapper
import de.dkh.cafemanagementbackend.wrapper.UserWrapper
import org.json.JSONArray
import org.json.JSONException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

class CafeUtils {

    companion object {

        fun getStringResponseFor(body: String, httpStatus: HttpStatus): ResponseEntity<String> =
            ResponseEntity(body, httpStatus)

        fun getUsersResponseFor(body: List<UserWrapper>, httpStatus: HttpStatus): ResponseEntity<List<UserWrapper>> =
            ResponseEntity(body, httpStatus)

        fun getCategoryResponseFor(
            body: List<CategoryWrapper>,
            httpStatus: HttpStatus
        ): ResponseEntity<List<CategoryWrapper>> =
            ResponseEntity(body, httpStatus)

        fun getProductResponseFor(
            body: List<ProductWrapper>,
            httpStatus: HttpStatus
        ): ResponseEntity<List<ProductWrapper>> =
            ResponseEntity(body, httpStatus)

        fun getSingleProductResponseFor(
            body: ProductWrapper?,
            httpStatus: HttpStatus
        ): ResponseEntity<ProductWrapper> =
            ResponseEntity(body, httpStatus)

        fun getUUID(): String = "Bill-${Date().time}"

        @Throws(JSONException::class)
        fun getJSONArrayFromString(data: String): JSONArray = JSONArray(data)

        fun getBillResponseFor(
            body: List<BillWrapper>,
            httpStatus: HttpStatus
        ): ResponseEntity<List<BillWrapper>> =
            ResponseEntity(body, httpStatus)

        fun getBillDocumentResponseFor(body: ByteArray, httpStatus: HttpStatus): ResponseEntity<ByteArray> =
            ResponseEntity(body, httpStatus)
    }
}