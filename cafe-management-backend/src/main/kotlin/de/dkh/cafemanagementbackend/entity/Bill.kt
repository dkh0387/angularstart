package de.dkh.cafemanagementbackend.entity

import com.fasterxml.jackson.annotation.JsonAutoDetect
import de.dkh.cafemanagementbackend.utils.mapper.BillMapper
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "bill")
@DynamicInsert
@DynamicUpdate
//@JsonIgnoreProperties(ignoreUnknown = true)
/**
Private fields within data class and by default, Jackson doesn't scan private fields for annotations.
We have to instruct it to do otherwise by putting @JsonAutoDetect annotation.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class Bill(
    @Column(name = "uuid") val uuid: String,
    @Column(name = "name") private val name: String,
    @Column(name = "email") private val email: String,
    @Column(name = "contact_number") private val contactNumber: String,
    @Column(name = "payment_method") private val paymentMethod: String,
    @Column(name = "total") private val total: Double,
    @Column(name = "product_details", columnDefinition = "json") private val productDetails: String,
    @Column(name = "created_by") private val createdBy: String
) : PersistentObject() {
    fun toBillMapper(): BillMapper {
        val billMapper = BillMapper()
        billMapper.uuid = this.uuid
        billMapper.name = this.name
        billMapper.contactNumber = this.contactNumber
        billMapper.email = this.email
        billMapper.paymentMethod = this.paymentMethod
        billMapper.productDetails = this.productDetails
        billMapper.total = this.total
        billMapper.isGenerate = false
        return billMapper
    }

    @Column(name = "document")
    lateinit var document: ByteArray

    companion object {

        fun createFromMapper(billMapper: BillMapper, currentUser: UserDetails?): Bill =
            Bill(
                billMapper.uuid,
                billMapper.name,
                billMapper.email,
                billMapper.contactNumber,
                billMapper.paymentMethod,
                billMapper.total,
                billMapper.productDetails,
                currentUser!!.username
            )
    }
}