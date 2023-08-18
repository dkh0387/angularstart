package de.dkh.cafemanagementbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CafeManagementBackendApplication

fun main(args: Array<String>) {
	runApplication<CafeManagementBackendApplication>(*args)
}
