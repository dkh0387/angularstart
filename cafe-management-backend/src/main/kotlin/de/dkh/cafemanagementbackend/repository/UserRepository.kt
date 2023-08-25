package de.dkh.cafemanagementbackend.repository

import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.wrapper.UserWrapper
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface UserRepository : JpaRepository<User, Long> {

    /**
     * NOTE: we do need the @Modifying annotation, otherwise we are not able to use custom change queries!
     */
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.status = ?2 WHERE u.id = ?1")
    fun updateStatus(id: Long, status: String): Int

    /**
     * Example of a JAVA constructor call inside a named query using JPA.
     */
    @Query("SELECT new de.dkh.cafemanagementbackend.wrapper.UserWrapper(u.id, u.name, u.email, u.contactNumber, u.status) FROM User u WHERE upper(u.role) = upper(?1)")
    fun getAllAdmins(role: String): List<UserWrapper>


    /**
     * NOTE: imlementation directly in [de.dkh.cafemanagementbackend.entity.User]!
     */
    fun findByEmail(@Param("emailInput") email: String?): User?

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.email =:emailInput")
    fun deleteByEmail(@Param("emailInput") email: String?): Int

}