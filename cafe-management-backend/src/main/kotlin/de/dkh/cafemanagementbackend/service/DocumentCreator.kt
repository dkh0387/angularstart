package de.dkh.cafemanagementbackend.service

import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import de.dkh.cafemanagementbackend.constants.CafeConstants
import de.dkh.cafemanagementbackend.utils.CafeUtils
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import de.dkh.cafemanagementbackend.utils.mapper.BillMapper
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import java.util.stream.Stream

/**
 * Creating a PDF bill as a document.
 * Either save it locally or persist byte array in the db.
 * @TODO: testing????
 */
@Component
class DocumentCreator {

    fun createAndSaveBill(billMapper: BillMapper, header: String, filePath: String): ByteArray {
        //if we want to store the document as bytearray in the db
        val baos = ByteArrayOutputStream()

        val document = Document()/*        val pdfWriter = PdfWriter.getInstance(
                    document, FileOutputStream(filePath)
                )*/
        val pdfWriter = PdfWriter.getInstance(document, baos)
        document.open()
        setRectangleInDocument(document)
        //create title
        val title = Paragraph(CafeConstants.BILL_TITLE, getFont(CafeConstants.TITLE_TYPE))
        title.alignment = Element.ALIGN_CENTER
        document.add(title)
        //create header
        val header = Paragraph("$header\n \n", getFont(CafeConstants.HEADER_FOOTER_TYPE))
        document.add(header)
        // create table with billing positions
        val table = PdfPTable(5)
        table.widthPercentage = 100.0F
        addTableHeader(table)
        //provide bill data
        val jsonArray = CafeUtils.getJSONArrayFromString(billMapper.productDetails)
        //fill table with data
        for (i in 0 until jsonArray.length()) {
            addRow(table, ServiceUtils.getMapFromJSON(jsonArray.getString(i)))
        }
        document.add(table)
        //create footer
        val footer = Paragraph(
            "Total: ${billMapper.total} \n ${CafeConstants.BILL_FOOTER_TEXT}", getFont(CafeConstants.HEADER_FOOTER_TYPE)
        )
        document.add(footer)
        document.close()
        pdfWriter.flush()
        //document as byteArray to persist...
        return baos.toByteArray()
    }

    /**
     * Design of the bill document (rectangle with a border).
     */
    @Throws(DocumentException::class)
    private fun setRectangleInDocument(document: Document) {
        println("Inside setRectangleInDocument")

        val rect = Rectangle(577.0F, 825.0F, 18.0F, 15.0F)
        rect.enableBorderSide(1)
        rect.enableBorderSide(2)
        rect.enableBorderSide(4)
        rect.enableBorderSide(8)
        rect.border = 2
        rect.borderColor = BaseColor.BLACK
        rect.borderWidth = 1.0F
        document.add(rect)
    }

    /**
     * Get a font depending from usage (title, header etc.)
     */
    private fun getFont(type: String): Font {
        return when (type) {
            CafeConstants.TITLE_TYPE -> {
                val font = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18f, BaseColor.BLACK)
                font.style = Font.BOLD
                return font
            }

            CafeConstants.HEADER_FOOTER_TYPE -> {
                val font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11f, BaseColor.BLACK)
                font.style = Font.BOLD
                font
            }

            else -> {
                return Font()
            }
        }
    }

    /**
     * Creates table header.
     */
    private fun addTableHeader(table: PdfPTable) {
        println("Inside addTableHeader")
        Stream.of("name", "Category", "Quantity", "Price", "Sub Total").forEach { columnTitle ->
            run {
                val header = PdfPCell()
                header.backgroundColor = BaseColor.LIGHT_GRAY
                header.borderWidth = 2.0F
                header.phrase = Phrase(columnTitle)
                header.backgroundColor = BaseColor.YELLOW
                header.horizontalAlignment = Element.ALIGN_CENTER
                header.verticalAlignment = Element.ALIGN_CENTER
                table.addCell(header)
            }
        }
    }

    /**
     * Add row data to the table.
     */
    private fun addRow(table: PdfPTable, data: Map<String, Any>) {
        println("Inside addRow")

        table.addCell(data["name"] as String)
        table.addCell(data["category"] as String)
        table.addCell(data["quantity"] as String)
        table.addCell(Double.toString().plus(data["price"] as Double))
        table.addCell(Double.toString().plus(data["total"] as Double))
    }

}
