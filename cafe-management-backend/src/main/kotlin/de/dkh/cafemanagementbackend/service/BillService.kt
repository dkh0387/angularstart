package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.wrapper.BillWrapper
import org.springframework.http.ResponseEntity

interface BillService {
    fun generateBill(requestMap: Map<String, Any>): ResponseEntity<String>
    fun getBills(): ResponseEntity<List<BillWrapper>>
    fun getBillDocument(requestMap: Map<String, String>): ResponseEntity<ByteArray>

}