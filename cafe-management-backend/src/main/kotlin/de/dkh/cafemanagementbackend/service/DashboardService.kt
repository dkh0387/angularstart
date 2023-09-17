package de.dkh.cafemanagementbackend.service

import org.springframework.http.ResponseEntity

interface DashboardService {
    fun getDetails(): ResponseEntity<Map<String, Any>>
}