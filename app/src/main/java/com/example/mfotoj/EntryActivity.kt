package com.example.mfotoj

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity

import android.os.Bundle
import android.content.Intent
import com.example.mfotoj.models.Lugar
import android.widget.EditText
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.content.pm.PackageManager
import android.hardware.Camera
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.File.createTempFile
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class EntryActivity : Activity(), Camera.PictureCallback {

    private val neededPermissions = arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        // Check code to get permissions if needed.
        val result = checkPermission()
        if (result) {
            // if permissions granted, update ui accordingly.
        }


        findViewById<View>(R.id.entry_image_button_camera).setOnClickListener { takePicture() }
        findViewById<View>(R.id.entry_button_add).setOnClickListener { }
        findViewById<View>(R.id.entry_button_add).setOnClickListener { submitEntry() }
        val intent = intent
        if (intent.hasExtra("edit_drink"))
        {
            val editableDrink: Lugar? = intent.getParcelableExtra("edit_drink")
            val editComment = findViewById<View>(R.id.entry_edit_text_comment) as EditText
            editComment.setText(editableDrink?.comments)
            if (editableDrink?.imageUri != null) {
                mUri = Uri.parse(editableDrink?.imageUri)
                val bitmap = bitmapFromUri
                val preview = findViewById<View>(R.id.entry_image_view_preview) as ImageView
                preview.setImageBitmap(bitmap)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermission(): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion>=android.os.Build.VERSION_CODES.M) {
            val permissionNotGranted= ArrayList<String>()
            for (permission in neededPermissions)
            {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    permissionNotGranted.add(permission)
                }
            }
            if (permissionNotGranted.size >0)
            {
                var shouldShowAlert = false
                for (permission in permissionNotGranted)
                {
                    shouldShowAlert = ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
                }
                val arr=arrayOfNulls<String>(permissionNotGranted.size)
                val permissions =permissionNotGranted.toArray(arr)
                if (shouldShowAlert)
                {
                    showPermissionAlert(permissions)
                } else {
                    requestPermissions(permissions)
                }
                return false
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionAlert(permissions: Array<String?>?) {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle(R.string.permission_required)
        alertBuilder.setMessage(R.string.permission_message)
        alertBuilder.setPositiveButton(android.R.string.yes)
        { _, _ ->
            if (permissions != null) {
                requestPermissions(permissions)
            }
        }
        val alert = alertBuilder.create()
        alert.show()
    }

    private fun requestPermissions(permissions: Array<String?>) {
        ActivityCompat.requestPermissions(this@EntryActivity, permissions, REQUEST_CODE)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                for (result in grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        // Not all permissions granted. Show some message and return.
                        return
                    }
                }

                // All permissions are granted. Do the work accordingly.
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val REQUEST_CODE = 100
    }



    private var mUri: Uri? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private fun takePicture() {

        lateinit var currentPhotoPath: String
        @Throws(IOException::class)
        fun createImageFile(): File
        {   // Create an image file name

            val filePhoto: File? = null

            try {
                val filePhoto = createImageFile()
            }catch (ex: IOException )
            {
                Log.e("DEBUG_TAG", "createFile", ex)
            }

            ///val filePhoto =  File(Environment.getExternalStorageDirectory(), "fff" )

            val time: String = (Date().time.toString() + "selfie.jpg")
            val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

            return  createTempFile( "JPEG_${time}_",".jpg",storageDir).apply { currentPhotoPath = absolutePath }

        }


        /// mUri = FileProvider.getUriForFile( applicationContext,"com.example.mfotoj.fileprovider", filePhoto)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            ////"android.media.action.IMAGE_CAPTURE");


            .also {
                    takePictureIntent -> takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                }catch (ex: IOException){
                    // Error occurred while creating the File
                    ////...
                    null
                }
            }
            }


        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        if (intent.resolveActivity(packageManager)?.also{

                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ///...
                    null
                }

            }

            != null)
        {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras = data.extras
            val bitmap = extras!!["data"] as Bitmap?
            /////getBitmapFromUri();
            val preview = findViewById<View>(R.id.entry_image_view_preview) as ImageView
            preview.setImageBitmap(bitmap)
        }
    }

    private val bitmapFromUri: Bitmap?
        get() {
            contentResolver.notifyChange(mUri!!, null)
            val resolver = contentResolver
            val bitmap: Bitmap
            return try {
                bitmap = MediaStore.Images.Media.getBitmap(resolver, mUri)
                bitmap
            } catch (e: Exception) {
                Toast.makeText(
                    this, e.message,
                    Toast.LENGTH_SHORT
                ).show()
                null
            }
        }

    private fun submitEntry() {
        val editComment = findViewById<View>(R.id.entry_edit_text_comment) as EditText
        val intent = Intent()
        intent.putExtra("comments", editComment.text.toString())
        if (mUri != null) {
            intent.putExtra("uri", "content://" + mUri!!.path.toString())
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onPictureTaken(p0: ByteArray?, p1: Camera?) {
        TODO("Not yet implemented")
    }
}