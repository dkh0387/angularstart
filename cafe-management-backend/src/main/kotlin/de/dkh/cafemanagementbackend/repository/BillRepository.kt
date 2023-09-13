package de.dkh.cafemanagementbackend.repository

import de.dkh.cafemanagementbackend.entity.Bill
import org.springframework.data.jpa.repository.JpaRepository

interface BillRepository : JpaRepository<Bill, Long> {
}