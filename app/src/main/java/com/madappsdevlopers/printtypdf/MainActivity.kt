package com.madappsdevlopers.printtypdf

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.widget.Button
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    val file_name:String="test_pdf.pdf"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dexter.withActivity(this)
            .withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object:PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    findViewById<Button>(R.id.printbtn).setOnClickListener{
                        createPDFFile(Common.getAppPath(this@MainActivity)+"test_pdf.pdf")

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {

                }

            })
            .check()
    }

    private fun createPDFFile(path: String) {

        if (File(path).exists())
            File(path).delete()

        try{
            val document=Document()
            PdfWriter.getInstance(document,FileOutputStream(path))

            document.open()
            document.pageSize = PageSize.A6
            document.addAuthor("PSPL Dispatch List")
            document.addCreator("PSPL")

            val colorAccent = BaseColor(0,153,204,255)
            val headingFontSize = 20.0f
            val valueFontSize = 26.0f

            val fontName = BaseFont.createFont("assets/fonts/grandhotelregular.otf","UTF-8",BaseFont.EMBEDDED)

            val titleStyle = Font(fontName,36.0f,Font.NORMAL,BaseColor.BLACK)

            addNewItem(document,"PSPL Dispatch Details", Element.ALIGN_CENTER,titleStyle)

            val headingStyle = Font(fontName,headingFontSize,Font.NORMAL,colorAccent)
            /////////
            document.close()
            printPDF()

        } catch (e:Exception){
            Log.e("PSPLERROR ", ""+e.message)
        }

    }

    private fun printPDF() {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager

        try{
            val printAdapter = PdfDocumentAdpter(this@MainActivity,Common.getAppPath(this@MainActivity)+"test_pdf.pdf")

            printManager.print("Document",printAdapter, PrintAttributes.Builder().build())

        } catch (e:Exception){
            Log.e("PSPL " ,""+e.message)
        }


    }

    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, align: Int, style: Font) {

        val chunk:Chunk = Chunk(text,style)
        val p = Paragraph(chunk)
        p.alignment = align
        document.add(p)

    }
}
