package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.Bill
import de.dkh.cafemanagementbackend.exception.GenerateReportException
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.repository.BillRepository
import de.dkh.cafemanagementbackend.utils.CafeUtils
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import de.dkh.cafemanagementbackend.utils.mapper.BillMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service


@Service
class BillServiceImpl(
    private val billRepository: BillRepository,
    private val jwtFilter: JwtFilter,
    private val documentCreator: DocumentCreator
) : BillService, LoggerService {

    private val logger: Logger = LoggerFactory.getLogger(CategoryServiceImpl::class.java)

    /**
     * @TODO: testing!
     */
    override fun generateReport(requestMap: Map<String, Any>): ResponseEntity<String> {
        println("Inside generateReport $requestMap")

        try {
            val fileName: String
            val billMapper =
                ServiceUtils.getMapperFromRequestObjectMap(requestMap, BillMapper::class.java) as BillMapper
            //If the file already exists, otherwise create new
            if (!billMapper.isGenerate!!) {
                fileName = billMapper.uuid
            } else {
                fileName = CafeUtils.getUUID()
                billMapper.uuid = fileName
                billRepository.save(Bill.createFromMapper(billMapper, jwtFilter.getCurrentUser()))
            }
            val header =
                "Name: ${billMapper.name} \n Contact Number: ${billMapper.contactNumber} \n Email: ${billMapper.email} \n Payment method: ${billMapper.paymentMethod}"
            val filePath = CafeConstants.STORE_LOCATION + fileName + CafeConstants.PDF_FILE_EXTENSION
            documentCreator.createAndSaveBill(billMapper, header, filePath)
            return CafeUtils.getStringResponseFor("{\"uuid:\":\" $fileName \"}", HttpStatus.OK)
        } catch (e: Exception) {
            logAndThrow(
                logger,
                CafeConstants.GENERATE_REPORT_WENT_WRONG,
                e,
                GenerateReportException(CafeConstants.GENERATE_REPORT_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
        return CafeUtils.getStringResponseFor(
            CafeConstants.GENERATE_REPORT_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR
        )
    }


}
