package com.madappsdevlopers.printtypdf

import android.content.Context
import android.os.Environment
import java.io.File

object Common {
    fun getAppPath(context:Context):String{
        //val dir = File(android.os.Environment.getExternalStorageDirectory().toSt
        // ring()
        //val dir = File(context.getExternalFilesDir(null).toString()

        val dir = File(context.getExternalFilesDir("/").toString()
        +File.separator
        +context.resources.getString(R.string.app_name)
        +File.separator)
        if (!dir.exists())
            dir.mkdir()
        return dir.path+File.separator
    }
}