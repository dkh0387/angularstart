package de.dkh.cafemanagementbackend.entity

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonManagedReference
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import de.dkh.cafemanagementbackend.utils.mapper.CategoryMapper
import de.dkh.cafemanagementbackend.wrapper.CategoryWrapper
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.NamedQuery


@NamedQuery(
    name = "Category.getAllCategoryWithActiveProdukt",
    query = "SELECT c FROM Category c WHERE id IN (SELECT p.category FROM Product p WHERE p.status = 'true')"
)
@Entity
@Table(name = "category")
@DynamicInsert
@DynamicUpdate
//@JsonIgnoreProperties(ignoreUnknown = true)
/**
Private fields within data class and by default, Jackson doesn't scan private fields for annotations.
We have to instruct it to do otherwise by putting @JsonAutoDetect annotation.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class Category(
    @Column(name = "name") var name: String,
    @OneToMany(mappedBy = "category") @JsonManagedReference var products: List<Product>?
) : PersistentObject() {

    /**
     * NOTE: Jackson needs this constructor by deserialization of [de.dkh.cafemanagementbackend.entity.Product] object,
     * which contains a category.
     */
    constructor(jsonString: String) : this(
        (ServiceUtils.objectMapper.readValue(jsonString, Category::class.java) as Category).name,
        (ServiceUtils.objectMapper.readValue(jsonString, Category::class.java) as Category).products
    )

    fun toWrapper(): CategoryWrapper = CategoryWrapper(this.id, this.name)

    companion object {
        fun createFromMapper(categoryMapper: CategoryMapper): Category = Category(categoryMapper.name, emptyList())
    }

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}
