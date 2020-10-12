package com.madappsdevlopers.printtypdf

import android.content.Context
import android.icu.util.Output
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.util.Log
import java.io.*
import java.lang.Exception

class PdfDocumentAdpter (context: Context,path:String) : PrintDocumentAdapter(){

    internal var context: Context?=null
    internal var path = ""

    init{
        this.context = context;
        this.path = path;


    }

    override fun onLayout(
        p0: PrintAttributes?,// oldAttributes = p0
        p1: PrintAttributes?,// newattributes = p1
        cancellationSignal: CancellationSignal?,
        layoutResultcallback: LayoutResultCallback?,
        p4: Bundle?// exteras = p4
    ) {
        if (cancellationSignal!!.isCanceled)
            layoutResultcallback!!.onLayoutCancelled()
        else{

            val builder = PrintDocumentInfo.Builder("file_name")
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                .build()

            layoutResultcallback!!.onLayoutFinished(builder.build(),p1!=p0)

        }

    }

    override fun onWrite(
        pageRanges: Array<out PageRange>?,
        parcelFileDescriptor: ParcelFileDescriptor?,// destination = p1
        cancellationSignal: CancellationSignal?,//cancellationSignal=p2
        writeResultcallback: WriteResultCallback?//callback=p3
    ) {
        var `in` : InputStream?=null
        var out: OutputStream?=null

        try{
            val file = File(path)

            `in` = FileInputStream(file)
            out = FileOutputStream(parcelFileDescriptor!!.fileDescriptor)

            if (!cancellationSignal!!.isCanceled){
                `in`.copyTo(out)
                writeResultcallback!!.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
            }
            else
                writeResultcallback!!.onWriteCancelled()
        } catch (e:Exception){

            writeResultcallback!!.onWriteFailed(e.message)
            Log.e("err ",""+e.message)


        } finally {

            try{
                `in`!!.close()
                out!!.close()


            } catch (e:IOException){
                Log.e("err ",""+e.message)
            }
        }

    }


}