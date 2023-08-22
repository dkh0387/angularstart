package de.dkh.cafemanagementbackend.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping(path = ["/user"])
interface UserREST {

    @PostMapping(path = ["/signup"])
    fun signUp(@RequestBody(required = true) requestMap: Map<String, String>): ResponseEntity<String>

    @PostMapping("/login")
    fun logIn(@RequestBody(required = true) requestMap: Map<String, String>): ResponseEntity<String>
}