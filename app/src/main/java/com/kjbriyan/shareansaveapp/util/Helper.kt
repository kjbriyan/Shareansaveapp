package com.kjbriyan.shareansaveapp.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.provider.MediaStore
import android.util.Base64
import androidx.core.app.ActivityCompat.startActivityForResult
import java.io.ByteArrayOutputStream


class Helper {


    fun moveActivity(context: Context?, tujuan: Class<*>) {
        val i = Intent(context, tujuan)
        context?.startActivity(i)
    }

    fun debuger(text: String) {
        println("Debug : $text")
    }

    fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap): String? {
        val stream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

        val byteFormat = stream.toByteArray()
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP)
    }



}