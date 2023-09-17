package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.service.DashboardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class DashboardRESTImpl(private val dashboardService: DashboardService) : DashboardREST {
    override fun getDetails(): ResponseEntity<Map<String, Any>> {
        return dashboardService.getDetails()
    }
}