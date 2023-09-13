package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.exception.GenerateReportException
import de.dkh.cafemanagementbackend.utils.CafeUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class BillServiceImpl : BillService, LoggerService {

    private val logger: Logger = LoggerFactory.getLogger(CategoryServiceImpl::class.java)

    override fun generateReport(requestMap: Map<String, Any>): ResponseEntity<String> {
        try {
            println("")
        } catch (e: Exception) {
            logAndThrow(
                logger,
                CafeConstants.GENERATE_REPORT_WENT_WRONG,
                e,
                GenerateReportException(CafeConstants.GENERATE_REPORT_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
        return CafeUtils.getStringResponseFor(
            CafeConstants.GENERATE_REPORT_WENT_WRONG,
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }


}