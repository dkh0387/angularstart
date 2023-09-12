package de.dkh.cafemanagementbackend.entity

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import de.dkh.cafemanagementbackend.utils.mapper.ProductMapper
import de.dkh.cafemanagementbackend.wrapper.ProductWrapper
import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate


@NamedQuery(
    name = "Product.getAllProduct",
    query = "SELECT new de.dkh.cafemanagementbackend.wrapper.ProductWrapper(p.id, p.name, p.description,p.price,p.status,p.category.id, p.category.name) FROM Product p"
)
@NamedQuery(name = "Product.updateStatus", query = "UPDATE Product p SET p.status = :status WHERE p.id =:id")

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
    @Column(name = "name") var name: String,
    @Column(name = "description") var description: String,
    @Column(name = "price") var price: Double,
    @Column(name = "status") var status: String,
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
            productMapper.status!!,
            productMapper.getCategory().get()
        )
    }

    fun toWrapper(): ProductWrapper = ProductWrapper(
        this.id,
        this.name,
        this.description,
        this.price,
        this.status,
        this.category.id,
        this.category.name
    )

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }

}