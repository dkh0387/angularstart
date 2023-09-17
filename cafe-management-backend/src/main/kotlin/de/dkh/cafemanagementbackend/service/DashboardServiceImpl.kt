package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.exception.GetDashboardDetailsException
import de.dkh.cafemanagementbackend.repository.BillRepository
import de.dkh.cafemanagementbackend.repository.CategoryRepository
import de.dkh.cafemanagementbackend.repository.ProductRepository
import de.dkh.cafemanagementbackend.utils.CafeUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class DashboardServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository,
    private val billRepository: BillRepository
) : DashboardService, LoggerService {

    private val logger: Logger = LoggerFactory.getLogger(DashboardServiceImpl::class.java)
    override fun getDetails(): ResponseEntity<Map<String, Any>> {
        try {
            return CafeUtils.getDashboardResponseFor(
                mapOf(
                    "category" to categoryRepository.count(),
                    "product" to productRepository.count(),
                    "bill" to billRepository.count()
                ), HttpStatus.OK
            )

        } catch (e: Exception) {
            logAndThrow(
                logger, CafeConstants.GET_DETAILS_WENT_WRONG, e, GetDashboardDetailsException(
                    CafeConstants.GET_DETAILS_WENT_WRONG + " MESSAGE: " + e.localizedMessage,
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
        }
        return CafeUtils.getDashboardResponseFor(emptyMap(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}