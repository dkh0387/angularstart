package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.service.BillService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

/**
 * @TODO: testing!
 */
@RestController
class BillRESTImpl(private val billService: BillService) : BillREST {
    override fun generateReport(requestMap: Map<String, Any>): ResponseEntity<String> {
        return billService.generateReport(requestMap)
    }
}