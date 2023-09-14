package de.dkh.cafemanagementbackend.service

import org.springframework.http.ResponseEntity

interface BillService {
    fun generateBill(requestMap: Map<String, Any>): ResponseEntity<String>
}