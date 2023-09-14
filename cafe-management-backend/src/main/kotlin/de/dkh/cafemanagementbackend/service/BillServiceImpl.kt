package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.Bill
import de.dkh.cafemanagementbackend.exception.GenerateBillException
import de.dkh.cafemanagementbackend.exception.GetBillsException
import de.dkh.cafemanagementbackend.jsonwebtoken.JwtFilter
import de.dkh.cafemanagementbackend.repository.BillRepository
import de.dkh.cafemanagementbackend.utils.CafeUtils
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import de.dkh.cafemanagementbackend.utils.mapper.BillMapper
import de.dkh.cafemanagementbackend.wrapper.BillWrapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service


@Service
class BillServiceImpl(
    private val billRepository: BillRepository,
    private val jwtFilter: JwtFilter,
    private val billCreator: BillCreator
) : BillService, LoggerService {

    private val logger: Logger = LoggerFactory.getLogger(CategoryServiceImpl::class.java)

    override fun generateBill(requestMap: Map<String, Any>): ResponseEntity<String> {
        println("Inside generateReport $requestMap")

        try {
            val billMapper =
                ServiceUtils.getMapperFromRequestObjectMap(requestMap, BillMapper::class.java) as BillMapper
            //If the file already exists, otherwise create new
            if (billMapper.isGenerate!!) {
                billMapper.uuid = CafeUtils.getUUID()
            }
            val header =
                "Name: ${billMapper.name} \n Contact Number: ${billMapper.contactNumber} \n Email: ${billMapper.email} \n Payment method: ${billMapper.paymentMethod}"
            //val filePath = CafeConstants.STORE_LOCATION + billMapper.uuid + CafeConstants.PDF_FILE_EXTENSION
            val bytes = billCreator.createAndPersist(billMapper, header)
            val bill = Bill.createFromMapper(billMapper, jwtFilter.getCurrentUser())
            bill.document = bytes
            // either an update or insert since uuid is a constraint
            billRepository.save(bill)
            return CafeUtils.getStringResponseFor("{\"uuid:\":\" ${billMapper.uuid} \"}", HttpStatus.OK)
        } catch (e: Exception) {
            logAndThrow(
                logger,
                CafeConstants.GENERATE_BILL_WENT_WRONG,
                e,
                GenerateBillException(CafeConstants.GENERATE_BILL_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
        return CafeUtils.getStringResponseFor(
            CafeConstants.GENERATE_BILL_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    /**
     * @TODO: testing!
     */
    override fun getBills(): ResponseEntity<List<BillWrapper>> {
        var bills = emptyList<BillWrapper>()
        try {
            bills = if (jwtFilter.isAdmin()) {
                billRepository.findAllAndOrderByIdDesc().map { it.toWrapper() }
            } else {
                billRepository.findAllByNameOrderByNameDesc(jwtFilter.getCurrentUser()!!.username)
                    .map { it.toWrapper() }
            }
            return CafeUtils.getBillResponseFor(bills, HttpStatus.OK)
        } catch (e: Exception) {
            logAndThrow(
                logger,
                CafeConstants.GET_BILLS_WENT_WRONG,
                e,
                GetBillsException(CafeConstants.GENERATE_BILL_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
        return CafeUtils.getBillResponseFor(bills, HttpStatus.OK)
    }

}
