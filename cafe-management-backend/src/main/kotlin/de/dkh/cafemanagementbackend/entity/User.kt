package de.dkh.cafemanagementbackend.entity

import com.fasterxml.jackson.annotation.JsonAutoDetect
import de.dkh.cafemanagementbackend.wrapper.UserWrapper
import jakarta.persistence.*
import lombok.Getter
import lombok.Setter
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails

/**
 * Example of usage named queries as implementation of a repository interface {@see UserRepository}.
 * NOTE: we can create named queries directly with right click on fields (using JPA designer plugin).
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
    @Column(name = "name") val name: String,
    @Column(name = "contact_number") val contactNumber: String,
    @Column(name = "email") val email: String,
    @Column(name = "password") val password: String?,
    @Column(name = "status") var status: String,
    @Column(name = "role") var role: String,
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", fetch = FetchType.EAGER)
    @Getter
    @Setter
    val authorities: List<Authority>? = null
) : PersistentObject() {

    fun toWrapper(): UserWrapper = UserWrapper(this.id, this.name, this.email, this.contactNumber, this.status)
    fun toUserDetails(): UserDetails = User(
        this.email,
        this.password,
        ArrayList()
    )


    enum class UserRoles {
        ROLE_ADMIN,
        ROLE_USER;

        fun nameWithoutPrefix(): String = this.name.substring(5)
    }

}
