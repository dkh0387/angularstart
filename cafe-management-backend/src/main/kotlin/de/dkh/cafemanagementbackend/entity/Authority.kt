package de.dkh.cafemanagementbackend.entity

import com.fasterxml.jackson.annotation.JsonAutoDetect
import jakarta.persistence.*
import lombok.Getter
import lombok.Setter
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate

@Entity
@Table(name = "authorities")
@DynamicInsert
@DynamicUpdate
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class Authority(val authority: String) : PersistentObject() {

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Getter
    @Setter
    private val user: User? = null

}
