package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.exception.GenerateBillException
import de.dkh.cafemanagementbackend.exception.GetBillsException
import de.dkh.cafemanagementbackend.service.BillService
import de.dkh.cafemanagementbackend.wrapper.BillWrapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class BillRESTImpl(private val billService: BillService) : BillREST {
    @Throws(GenerateBillException::class)
    override fun generateReport(requestMap: Map<String, Any>): ResponseEntity<String> {
        return billService.generateBill(requestMap)
    }

    /**
     * @TODO: testing!
     */
    @Throws(GetBillsException::class)
    override fun getBills(): ResponseEntity<List<BillWrapper>> {
        return billService.getBills()
    }
}