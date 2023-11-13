package de.dkh.cafemanagementbackend.service

import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.entity.Bill
import de.dkh.cafemanagementbackend.exception.DeleteBillException
import de.dkh.cafemanagementbackend.exception.GenerateBillException
import de.dkh.cafemanagementbackend.exception.GetBiilDocumentException
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
import java.util.*


@Service
class BillServiceImpl(
    private val billRepository: BillRepository, private val jwtFilter: JwtFilter, private val billCreator: BillCreator
) : BillService, LoggerService {

    private val logger: Logger = LoggerFactory.getLogger(CategoryServiceImpl::class.java)

    override fun generateBill(requestMap: Map<String, Any>): ResponseEntity<String> {
        println("Inside generateReport $requestMap")

        try {
            val bill = createFromMapperAndSave(requestMap)
            //return CafeUtils.getStringResponseFor("{\"uuid:\":\" ${bill.uuid} \"}", HttpStatus.OK)
            return CafeUtils.getStringResponseFor(CafeUtils.formatBodyAsJSON(bill.uuid), HttpStatus.OK)
        } catch (e: Exception) {
            logAndThrow(
                logger,
                CafeConstants.GENERATE_BILL_WENT_WRONG,
                e,
                GenerateBillException(CafeConstants.GENERATE_BILL_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
        return CafeUtils.getStringResponseFor(
            CafeUtils.formatBodyAsJSON(CafeConstants.GENERATE_BILL_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    override fun getBills(): ResponseEntity<List<BillWrapper>> {
        println("Inside getBills")

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
        return CafeUtils.getBillResponseFor(bills, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /**
     * We can either get an existing one from the db or generate and persist and get a new one,
     * depending on if an uuid is provided in the request map.
     */
    override fun getBillDocument(requestMap: Map<String, String>): ResponseEntity<ByteArray> {
        println("Inside getBillDocument $requestMap")

        try {
            val billToCreate: Bill
            val billOptional: Optional<Bill>
            val billMapper =
                ServiceUtils.getMapperFromRequestObjectMap(requestMap, BillMapper::class.java) as BillMapper
            //Check whether the bill is already generated and persisted.
            // If so, get from the db, otherwise create, persist and return a new one
            if (billMapper.uuid == null) {
                billToCreate = createFromMapperAndSave(requestMap)
                billOptional = billRepository.findByUuid(billToCreate.uuid)
            } else {
                billOptional = billRepository.findByUuid(billMapper.uuid!!)
            }
            return CafeUtils.getBillDocumentResponseFor(billOptional.get().document, HttpStatus.OK)
        } catch (e: Exception) {
            logAndThrow(
                logger,
                CafeConstants.GET_BILL_DOCUMENT_WENT_WRONG,
                e,
                GetBiilDocumentException(CafeConstants.GET_BILL_DOCUMENT_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
        return CafeUtils.getBillDocumentResponseFor(ByteArray(0), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    override fun deleteBill(id: Long): ResponseEntity<String> {
        println("Inside deleteBill $id")

        try {
            val billOptional = billRepository.findById(id)

            return if (billOptional.isPresent) {
                billRepository.delete(billOptional.get())
                CafeUtils.getStringResponseFor(CafeConstants.DELETE_BILL_SUCCESSFULLY, HttpStatus.OK)
            } else {
                CafeUtils.getStringResponseFor(CafeConstants.DELETE_BILL_WENT_WRONG, HttpStatus.BAD_REQUEST)
            }
        } catch (e: Exception) {
            logAndThrow(
                logger,
                CafeConstants.DELETE_BILL_WENT_WRONG,
                e,
                DeleteBillException(CafeConstants.DELETE_BILL_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
        return CafeUtils.getStringResponseFor(CafeConstants.DELETE_BILL_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private fun createFromMapperAndSave(requestMap: Map<String, Any>): Bill {
        val billMapper =
            ServiceUtils.getMapperFromRequestObjectMap(requestMap, BillMapper::class.java) as BillMapper
        billMapper.uuid = CafeUtils.getUUID()
        val header =
            "Name: ${billMapper.name} \n Contact Number: ${billMapper.contactNumber} \n Email: ${billMapper.email} \n Payment method: ${billMapper.paymentMethod}"
        //val filePath = CafeConstants.STORE_LOCATION + billMapper.uuid + CafeConstants.PDF_FILE_EXTENSION
        val bytes = billCreator.createAndPersist(billMapper, header)
        val bill = Bill.createFromMapper(billMapper, jwtFilter.getCurrentUser())
        bill.document = bytes
        // either an update or insert since uuid is a constraint
        return billRepository.save(bill)
    }

}
