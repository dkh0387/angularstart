package de.dkh.cafemanagementbackend.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter


/**
 * Example of a super class for all entities having `ID` attribute.
 * To make it work, we need to annotate the class with [MappedSuperclass] in order
 * to tell hibernate to search for the id property in the super class.
 */
@MappedSuperclass
open class PersistentObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    protected var id: Long = 0


}
