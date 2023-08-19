package de.dkh.cafemanagementbackend.entity

import com.fasterxml.jackson.annotation.JsonAutoDetect
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.NamedQuery
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate

/**
 * Example of usage named queries as implementation of a repository interface {@see UserRepository}.
 */
@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email =: emailInput")

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
data class User(
    @Column(name = "name") private val name: String,
    @Column(name = "contact_number") private val contactNumber: String,
    @Column(name = "email") private val email: String,
    @Column(name = "password") private val password: String,
    @Column(name = "status") private val status: String,
    @Column(name = "role") private val role: String
) : PersistentObject()
