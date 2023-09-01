package de.dkh.cafemanagementbackend.entity

import com.fasterxml.jackson.annotation.JsonAutoDetect
import de.dkh.cafemanagementbackend.utils.mapper.CategoryMapper
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.NamedQuery

/**
 * No specific condition for now, coming soon...
 */
@NamedQuery(name = "Category.findAllForProduct", query = "SELECT c FROM Category c")
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
data class Category(@Column(name = "name") private val name: String) : PersistentObject() {
    companion object {
        fun createFromMapper(categoryMapper: CategoryMapper): Category = Category(categoryMapper.name)
    }
}
