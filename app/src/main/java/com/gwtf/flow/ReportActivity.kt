package com.gwtf.flow

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.PermissionRequest
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.Utilites.AmountCalculator
import com.gwtf.flow.Utilites.Constants.Business_Selected
import com.gwtf.flow.adapter.BooksReportAdapter
import com.gwtf.flow.model.DataModel
import com.itextpdf.text.*
import com.itextpdf.text.PageSize.A4
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.karumi.dexter.BuildConfig
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ReportActivity : AppCompatActivity() {

    var list = ArrayList<DataModel>()
    lateinit var adapter: BooksReportAdapter

    val colorPrimary = BaseColor(241, 241, 241)
    val FONT_SIZE_DEFAULT = 12f
    val FONT_SIZE_SMALL = 8f

    val PADDING_EDGE = 40f
    val TEXT_TOP_PADDING = 3f
    val TABLE_TOP_PADDING = 10f
    val TEXT_TOP_PADDING_EXTRA = 30f
    val BILL_DETAILS_TOP_PADDING = 80f

    var basfontLight: BaseFont =
        BaseFont.createFont("assets/fonts/lexend_light.ttf", "UTF-8", BaseFont.EMBEDDED)
    var appFontLight = Font(basfontLight, FONT_SIZE_SMALL)

    var basfontRegular: BaseFont =
        BaseFont.createFont("assets/fonts/lexend_regular.ttf", "UTF-8", BaseFont.EMBEDDED)
    var appFontRegular = Font(basfontRegular, FONT_SIZE_DEFAULT)


    var basfontSemiBold: BaseFont =
        BaseFont.createFont("assets/fonts/lexend_medium.ttf", "UTF-8", BaseFont.EMBEDDED)
    var appFontSemiBold = Font(basfontSemiBold, 24f)


    var basfontBold: BaseFont =
        BaseFont.createFont("assets/fonts/lexend_bold.ttf", "UTF-8", BaseFont.EMBEDDED)
    var appFontBold = Font(basfontBold, FONT_SIZE_DEFAULT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        val db = SqlDatabase(this)

        val totalOut = findViewById<TextView>(R.id.totalOut)
        val totalEnteries = findViewById<TextView>(R.id.totalEnteries)
        totalOut.text = "" + AmountCalculator.getOut(this)
        val totalIn = findViewById<TextView>(R.id.totalIn)
        totalIn.text = "" + AmountCalculator.getIn(this)

        val listReports = findViewById<RecyclerView>(R.id.listReports)
        listReports.isNestedScrollingEnabled = false
        val layoutManager = GridLayoutManager(this, 1)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        listReports.layoutManager = layoutManager

        list = db.getBookData(Business_Selected)
        adapter = BooksReportAdapter(list)
        listReports.adapter = adapter

        totalEnteries.text = "" + list.size + " Entries"

        val searchTxt = findViewById<EditText>(R.id.search_text)
        searchTxt.doOnTextChanged { text, start, before, count ->
            filter(searchTxt.text.toString())
        }

        val makeReportBtn = findViewById<Button>(R.id.makeReportBtn)
        makeReportBtn.setOnClickListener {
            Dexter.withActivity(this)
                .withPermissions(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                        if (report.areAllPermissionsGranted()) {

                            val doc = Document(A4, 0f, 0f, 0f, 0f)
                            val outPath =
                                getExternalFilesDir(null).toString() + "/invoice.pdf" //location where the pdf will store
                            Log.d("loc", outPath)
                            val writer = PdfWriter.getInstance(doc, FileOutputStream(outPath))
                            doc.open()
                            //Header Column Init with width nad no. of columns
                            initInvoiceHeader(doc)
                            doc.setMargins(0f, 0f, PADDING_EDGE, PADDING_EDGE)
//                            initBillDetails(doc)
//                            addLine(writer)
//                            initTableHeader(doc)
//                            initItemsTable(doc)
//                            initPriceDetails(doc)
//                            initFooter(doc)
                            doc.close()

                            val file = File(outPath)
                            val path: Uri = FileProvider.getUriForFile(
                                applicationContext,
                                BuildConfig.APPLICATION_ID + ".provider",
                                file
                            )
                            try {
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.setDataAndType(path, "application/pdf")
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                startActivity(intent)

                            } catch (e: ActivityNotFoundException) {

                            }

                        } else {

                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token!!.continuePermissionRequest()
                    }
                }).check()
        }

    }

    private fun filter(text: String) {
        val filteredlist: ArrayList<DataModel> = ArrayList()
        for (item in list) {
            if (item.partyName.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            adapter.filterList(filteredlist)
        }
    }


    private fun initInvoiceHeader(doc: Document) {
        val d = resources.getDrawable(R.drawable.logo2)
        val bitDw = d as BitmapDrawable
        val bmp = bitDw.bitmap
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = Image.getInstance(stream.toByteArray())
        val headerTable = PdfPTable(3)
        headerTable.isLockedWidth = true
        headerTable.totalWidth = A4.width // set content width to fill document
        val cell = PdfPCell(Image.getInstance(image)) // Logo Cell
        cell.border = Rectangle.NO_BORDER // Removes border
        cell.paddingTop = TEXT_TOP_PADDING_EXTRA // sets padding
        cell.paddingRight = TABLE_TOP_PADDING
        cell.paddingLeft = PADDING_EDGE
        cell.horizontalAlignment = Rectangle.ALIGN_LEFT
        cell.paddingBottom = TEXT_TOP_PADDING_EXTRA

        cell.backgroundColor = colorPrimary // sets background color
        cell.horizontalAlignment = Element.ALIGN_CENTER
        headerTable.addCell(cell) // Adds first cell with logo

        val contactTable =
            PdfPTable(1) // new vertical table for contact details
        val phoneCell =
            PdfPCell(
                Paragraph(
                    "Flow Report"
                )
            )
        phoneCell.border = Rectangle.NO_BORDER
        phoneCell.horizontalAlignment = Element.ALIGN_LEFT
        phoneCell.paddingTop = TEXT_TOP_PADDING

        contactTable.addCell(phoneCell)

        val emailCellCell = PdfPCell(Phrase("sreeharikariot@gmail.com"))
        emailCellCell.border = Rectangle.NO_BORDER
        emailCellCell.horizontalAlignment = Element.ALIGN_RIGHT
        emailCellCell.paddingTop = TEXT_TOP_PADDING

        contactTable.addCell(emailCellCell)

        val webCell = PdfPCell(Phrase("www.kariot.me"))
        webCell.border = Rectangle.NO_BORDER
        webCell.paddingTop = TEXT_TOP_PADDING
        webCell.horizontalAlignment = Element.ALIGN_RIGHT

        contactTable.addCell(webCell)


        val headCell = PdfPCell(contactTable)
        headCell.border = Rectangle.NO_BORDER
        headCell.horizontalAlignment = Element.ALIGN_RIGHT
        headCell.verticalAlignment = Element.ALIGN_MIDDLE
        headCell.backgroundColor = colorPrimary
        headerTable.addCell(headCell)

        val address = PdfPTable(1)
        val line1 = PdfPCell(
            Paragraph(
                "Address Line 1"
            )
        )
        line1.border = Rectangle.NO_BORDER
        line1.paddingTop = TEXT_TOP_PADDING
        line1.horizontalAlignment = Element.ALIGN_RIGHT

        address.addCell(line1)

        val line2 = PdfPCell(Paragraph("Address Line 2"))
        line2.border = Rectangle.NO_BORDER
        line2.paddingTop = TEXT_TOP_PADDING
        line2.horizontalAlignment = Element.ALIGN_RIGHT

        address.addCell(line2)

        val line3 = PdfPCell(Paragraph("Address Line 3"))
        line3.border = Rectangle.NO_BORDER
        line3.paddingTop = TEXT_TOP_PADDING
        line3.horizontalAlignment = Element.ALIGN_RIGHT

        address.addCell(line3)


        val addressHeadCell = PdfPCell(address)
        addressHeadCell.border = Rectangle.NO_BORDER
        addressHeadCell.setLeading(22f, 25f)
        addressHeadCell.horizontalAlignment = Element.ALIGN_RIGHT
        addressHeadCell.verticalAlignment = Element.ALIGN_MIDDLE
        addressHeadCell.backgroundColor = colorPrimary
        addressHeadCell.paddingRight = PADDING_EDGE
        headerTable.addCell(addressHeadCell)

        doc.add(headerTable)
    }


}