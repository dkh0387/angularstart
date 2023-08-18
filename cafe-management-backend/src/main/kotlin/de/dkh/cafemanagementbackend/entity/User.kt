package de.dkh.cafemanagementbackend.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import lombok.Data
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate

@Data
@Entity
@Table(name = "user")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties(ignoreUnknown = true)
class User(
    @Column(name = "name") private val name: String,
    @Column(name = "contact_number") private val contactNumber: String,
    @Column(name = "email") private val email: String,
    @Column(name = "password") private val password: String,
    @Column(name = "status") private val status: String,
    @Column(name = "role") private val role: String
) : PersistentObject() {


}
