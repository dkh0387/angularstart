package de.dkh.cafemanagementbackend.entity

import com.fasterxml.jackson.annotation.JsonAutoDetect
import de.dkh.cafemanagementbackend.utils.mapper.UserMapperFull
import de.dkh.cafemanagementbackend.utils.mapper.UserMapperSimple
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
    @Column(name = "password") var password: String?,
    @Column(name = "status") var status: String,
    @Column(name = "role") var role: String,
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", fetch = FetchType.EAGER)
    @Getter
    @Setter
    val authorities: List<Authority>? = null
) : PersistentObject() {

    constructor(
        id: Long?,
        name: String,
        contactNumber: String,
        email: String,
        password: String?,
        status: String,
        role: String, authorities: List<Authority>?
    ) : this(name, contactNumber, email, password, status, role, authorities) {
        if (id != null) {
            this.id = id
        }
    }

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

    companion object {

        val DEFAULT_STATUS = "false"
        val DEFAULT_ROLE = UserRoles.ROLE_USER.nameWithoutPrefix().lowercase()
        fun createFromSimple(userMapperSimple: UserMapperSimple): de.dkh.cafemanagementbackend.entity.User = User(
            userMapperSimple.name,
            userMapperSimple.contactNumber,
            userMapperSimple.email,
            userMapperSimple.password,
            DEFAULT_STATUS,
            DEFAULT_ROLE,
            null
        )

        fun createFromFull(userMapperFull: UserMapperFull): de.dkh.cafemanagementbackend.entity.User = User(
            userMapperFull.id,
            userMapperFull.name,
            userMapperFull.contactNumber,
            userMapperFull.email,
            userMapperFull.password,
            userMapperFull.status,
            userMapperFull.role,
            null
        )
    }

}
