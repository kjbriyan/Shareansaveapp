package com.kjbriyan.shareansaveapp.detail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kjbriyan.shareansaveapp.R
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.alert_edit.view.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class DetailActivity : AppCompatActivity() {
    val REQUEST_CODE_GALLERY = 11
    var filegambar: File? = null
    private val TAG: String = "buktii"
    var imgUri: Uri? = null
    private var mView1: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        checkPermissionsAndOpenFilePicker()
        val img = intent.getStringExtra("img")
        Picasso.get().load(img).into(imageVieww)
        btn_txt.setOnClickListener {
            alert()
        }
        btn_add.setOnClickListener {
            open_galey()
        }
        mView1 = findViewById<LinearLayout>(R.id.linearLayout)
        (mView1 as LinearLayout).isDrawingCacheEnabled = true
        (mView1 as LinearLayout).buildDrawingCache(true)
        btn_save.setOnClickListener {
            val bitmap: Bitmap
            (mView1 as LinearLayout).setDrawingCacheEnabled(true)
            bitmap = mView1!!.drawingCache
            saveImageToInternalStorage(mView1 as LinearLayout)
//            saveImageFile(bitmap,"saveimg")
        }
        btn_share.setOnClickListener {
            val bitmap: Bitmap
            imageVieww?.setDrawingCacheEnabled(true)
            bitmap = imageVieww!!.drawingCache


            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/jpeg"

            val bytes = ByteArrayOutputStream()
//
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

            val savedFile: String = saveImageFile(bitmap, "saveimg")

            val imageUri = Uri.parse(savedFile)
            share.putExtra(Intent.EXTRA_STREAM, imageUri)
            startActivity(Intent.createChooser(share, "Share Image"))

        }

    }



    fun alert() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_edit, null)
        //AlertDialogBuilder
        AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("add text")
            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->

//                mDialogView.iv_imgg?.setDrawingCacheEnabled(true)
                val txt = mDialogView.et_textt.text
                tv_text.text = " " + txt

            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                Toast.makeText(this, "Canceld", Toast.LENGTH_LONG).show()
            })
            .show()
        //show dialog

    }

    private fun open_galey() {
        val galery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(galery, REQUEST_CODE_GALLERY)
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                if (data != null) {
                    imgUri = data.data
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            this.contentResolver, imgUri
                        )
                        tmpFile(bitmap)
                        imageVieww.setImageBitmap(bitmap)

                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }


    private fun tmpFile(bitmap: Bitmap): File? {
        filegambar = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            System.currentTimeMillis().toString() + "_iamge.jpeg"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmadata: ByteArray = bos.toByteArray()
        try {
            val fos = FileOutputStream(filegambar)
            fos.write(bitmadata)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return filegambar
    }

    private fun checkPermissionsAndOpenFilePicker() {
        val permissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted) {
//            Toast.makeText(this, "Disetujui", Toast.LENGTH_SHORT).show()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                showError()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                showError()
            }
        }
    }

    fun saveImageFile(image: Bitmap, folder: String): String {
        val now = java.lang.Long.toString(Date().time)
        val imageFile = File(Environment.getExternalStorageDirectory().toString() + folder)
        if (!imageFile.exists()) {
            val screenShotsFolder = File("/sdcard/Pictures/$folder/")
            screenShotsFolder.mkdirs()
        }
        val imageName = File(File("/sdcard/Pictures/$folder/"), "$now.jpg")
        try {
            val outputStream = FileOutputStream(imageName)
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
//        Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
        return imageName.toString()
    }

    private fun saveImageToInternalStorage(v: View) {
        val b: Bitmap = Bitmap.createBitmap(v.getDrawingCache())
        v.setDrawingCacheEnabled(false)
        val bytes = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val c = Calendar.getInstance()

        val currentDate = SimpleDateFormat("yyyyMMdd_HH_mm_ss")
        val saveCurrentDate = currentDate.format(c.time)

        val root = Environment.getExternalStorageDirectory().toString()
        val folder = File("$root/saveimg")
        folder.mkdir()

        val my_file = File(folder, "$saveCurrentDate.png")
        try {
            my_file.createNewFile()
            val fo = FileOutputStream(my_file)
            fo.write(bytes.toByteArray())
            fo.flush()
            fo.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 0
    }
}