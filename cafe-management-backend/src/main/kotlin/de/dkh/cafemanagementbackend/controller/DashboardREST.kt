package de.dkh.cafemanagementbackend.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/dashboard")
interface DashboardREST {

    @GetMapping("/details")
    fun getDetails(): ResponseEntity<Map<String, Any>>
}