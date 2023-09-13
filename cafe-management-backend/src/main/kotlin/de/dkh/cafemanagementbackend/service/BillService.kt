package de.dkh.cafemanagementbackend.service

import org.springframework.http.ResponseEntity

interface BillService {
    fun generateReport(requestMap: Map<String, Any>): ResponseEntity<String>
}