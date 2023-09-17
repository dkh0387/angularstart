package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.exception.GenerateBillException
import de.dkh.cafemanagementbackend.exception.GetBiilDocumentException
import de.dkh.cafemanagementbackend.exception.GetBillsException
import de.dkh.cafemanagementbackend.service.BillService
import de.dkh.cafemanagementbackend.wrapper.BillWrapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController


@RestController
class BillRESTImpl(private val billService: BillService) : BillREST {
    @Throws(GenerateBillException::class)
    override fun generateReport(requestMap: Map<String, Any>): ResponseEntity<String> {
        return billService.generateBill(requestMap)
    }

    @Throws(GetBillsException::class)
    override fun getBills(): ResponseEntity<List<BillWrapper>> {
        return billService.getBills()
    }

    @Throws(GetBiilDocumentException::class)
    override fun getBillDocument(requestMap: Map<String, String>): ResponseEntity<ByteArray> {
        val billDocument = billService.getBillDocument(requestMap)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_PDF
        headers.cacheControl = "must-revalidate, post-check=0, pre-check=0"
        return ResponseEntity<ByteArray>(billDocument.body as ByteArray, headers, HttpStatus.OK)
    }

    override fun deleteBill(id: Long): ResponseEntity<String> {
        return billService.deleteBill(id)
    }
}