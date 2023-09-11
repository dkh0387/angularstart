package de.dkh.cafemanagementbackend.utils.mapper

import com.fasterxml.jackson.annotation.JsonAutoDetect
import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.Category
import de.dkh.cafemanagementbackend.repository.CategoryRepository
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import java.util.*

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
@AllArgsConstructor
class ProductMapper() : KeyMapper() {

    lateinit var categoryRepository: CategoryRepository

    var id: Long? = null
    var name: String = ""
    var description: String = ""
    var price: Double = 0.0
    var status: String? = null
    var categoryId: Long? = null


    fun getCategory(): Optional<Category> {
        return if (categoryId == null) {
            Optional.of(Category(CafeConstants.DEFAULT_PRODUCT_CATEGORY_NAME))
        } else {
            categoryRepository.findById(categoryId!!)
        }
    }

}