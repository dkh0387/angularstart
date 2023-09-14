package de.dkh.cafemanagementbackend.repository

import de.dkh.cafemanagementbackend.entity.Bill
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.transaction.annotation.Transactional

interface BillRepository : JpaRepository<Bill, Long> {

    @Transactional
    @Modifying
    fun deleteByUuid(uuid: String)
}