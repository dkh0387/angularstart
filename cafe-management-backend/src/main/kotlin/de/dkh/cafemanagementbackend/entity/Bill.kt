package de.dkh.cafemanagementbackend.entity

import com.fasterxml.jackson.annotation.JsonAutoDetect
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate

@Entity
@Table(name = "user")
@DynamicInsert
@DynamicUpdate
//@JsonIgnoreProperties(ignoreUnknown = true)
/**
Private fields within data class and by default, Jackson doesn't scan private fields for annotations.
We have to instruct it to do otherwise by putting @JsonAutoDetect annotation.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class Bill(
    @Column(name = "uuid") private val uuid: String,
    @Column(name = "name") private val name: String,
    @Column(name = "email") private val email: String,
    @Column(name = "contactnumber") private val contactNumber: String,
    @Column(name = "paymentmethod") private val paymentMethod: String,
    @Column(name = "total") private val total: Double,
    @Column(name = "productdetails", columnDefinition = "json") private val productDetails: String,
    @Column(name = "createdby") private val createdBy: String
) : PersistentObject() {
}