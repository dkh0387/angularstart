package de.dkh.cafemanagementbackend.entity

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import de.dkh.cafemanagementbackend.utils.mapper.ProductMapper
import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate

@Entity
@Table(name = "product")
@DynamicInsert
@DynamicUpdate
//@JsonIgnoreProperties(ignoreUnknown = true)
/**
Private fields within data class and by default, Jackson doesn't scan private fields for annotations.
We have to instruct it to do otherwise by putting @JsonAutoDetect annotation.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
data class Product(
    @Column(name = "name") val name: String,
    @Column(name = "description") val description: String,
    @Column(name = "price") val price: Double,
    @Column(name = "status") val status: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(
        name = "category_id",
        nullable = false
    ) var category: Category
) : PersistentObject() {
    companion object {
        fun createFromMapper(productMapper: ProductMapper): Product = Product(
            productMapper.name,
            productMapper.description,
            productMapper.price,
            productMapper.status,
            productMapper.getCategory().get()
        )
    }

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }

}