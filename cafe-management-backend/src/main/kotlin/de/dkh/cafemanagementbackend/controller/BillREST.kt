package de.dkh.cafemanagementbackend.controller

import de.dkh.cafemanagementbackend.wrapper.BillWrapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/bill")
interface BillREST {

    @PostMapping("/generate")
    fun generateReport(@RequestBody requestMap: Map<String, Any>): ResponseEntity<String>

    @GetMapping("/get")
    fun getBills(): ResponseEntity<List<BillWrapper>>

    @PostMapping("/getBillDocument")
    fun getBillDocument(@RequestBody requestMap: Map<String, String>): ResponseEntity<ByteArray>
}