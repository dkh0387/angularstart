package de.dkh.cafemanagementbackend.utils.mapper

import com.fasterxml.jackson.annotation.JsonAutoDetect
import de.dkh.cafemanagementbackend.entity.Category
import de.dkh.cafemanagementbackend.repository.CategoryRepository
import de.dkh.cafemanagementbackend.utils.ServiceUtils
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
    var status: String = ""
    var categoryId: Long? = null

    constructor(jsonString: String) : this() {
        (ServiceUtils.objectMapper.readValue(jsonString, ProductMapper::class.java) as ProductMapper).id
        (ServiceUtils.objectMapper.readValue(jsonString, ProductMapper::class.java) as ProductMapper).name
        (ServiceUtils.objectMapper.readValue(jsonString, ProductMapper::class.java) as ProductMapper).description
        (ServiceUtils.objectMapper.readValue(jsonString, ProductMapper::class.java) as ProductMapper).price
        (ServiceUtils.objectMapper.readValue(jsonString, ProductMapper::class.java) as ProductMapper).status
        (ServiceUtils.objectMapper.readValue(jsonString, ProductMapper::class.java) as ProductMapper).categoryId
    }


    fun getCategory(): Optional<Category> {
        return if (categoryId == null) {
            Optional.of(Category.defaultCategory)
        } else {
            categoryRepository.findById(categoryId!!)
        }
    }

}