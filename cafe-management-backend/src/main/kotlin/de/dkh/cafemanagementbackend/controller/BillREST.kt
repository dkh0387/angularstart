package de.dkh.cafemanagementbackend.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/bill")
interface BillREST {

    @PostMapping("/generateReport")
    fun generateReport(@RequestBody requestMap: Map<String, Any>): ResponseEntity<String>
}