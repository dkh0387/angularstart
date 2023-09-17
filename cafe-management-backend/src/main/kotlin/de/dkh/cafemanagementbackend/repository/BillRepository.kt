package de.dkh.cafemanagementbackend.repository

import de.dkh.cafemanagementbackend.entity.Bill
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface BillRepository : JpaRepository<Bill, Long> {

    @Transactional
    @Modifying
    fun deleteByUuid(uuid: String)

    fun findAllByNameOrderByNameDesc(@Param("createdBy") createdBy: String): List<Bill>
    fun findAllAndOrderByIdDesc(): List<Bill>

    fun findByUuid(uuid: String): Optional<Bill>

    @Transactional
    @Modifying
    fun deleteByName(name: String)
}