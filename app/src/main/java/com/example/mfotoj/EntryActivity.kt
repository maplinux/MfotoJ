package com.example.mfotoj

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mfotoj.models.Lugar
import java.io.File
import java.io.IOException
import java.util.*

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
        findViewById<View>(R.id.entry_button_add).setOnClickListener { submitEntry() }


        val intent = intent
        //edit_lugar??
        if (intent.hasExtra("edit_lugar"))
        {
            val editableLugar: Lugar? = intent.getParcelableExtra("edit_lugar")
            val editComment = findViewById<View>(R.id.entry_edit_text_comment) as EditText
            editComment.setText(editableLugar?.comments)
            if (editableLugar?.imageUri != null) {
                var mUri = Uri.parse(editableLugar?.imageUri)
                val bitmap = bitmapFromUri

               ////////////////////////


                ////////////////////////
                //preview.setImageBitmap(bitmap)
                ////////////////////////////
            }
        }

    }

    private var mUri: Uri? = null
    private val REQUEST_IMAGE_CAPTURE = 1

    private fun takePicture() { //old dispatchTakePictureIntent

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager).also {


                // Create the File where the photo should go
                val photoFile: File? = try
                {
                    createImageFile()
                }
                catch (ex: IOException)
                {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also { val mUri: Uri = FileProvider.getUriForFile(
                    this,"com.example.mfotoj.fileprovider", it
                )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri) //cambio  a mUri

                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }              /////* mUri = Uri.fromFile(absolutePath)  // change (filePhoto) por currentPhotoPhat

            setPic()

        Log.d(REQUEST_CODE.toString(), "takePicture: intent ")
        }
    }

  /*  //miniature
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            val imageBitmat = data?.extras?.get("data") as Bitmap
            val preview = findViewById<View>(R.id.entry_image_view_preview) as ImageView
            preview.setImageBitmap(imageBitmat)
        }
    }*/

    lateinit var currentPhotoPath: String
    @Throws(IOException::class)

    fun createImageFile():File
    {
        val time: String =(Date().time.toString()+ "selfie")
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile( "MP_${time}_",".jpg",storageDir)
            .apply { currentPhotoPath = absolutePath }
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

    private fun setPic() {


        ////////////////////////////
        /////////////////val preview
        val imageView = findViewById<View>(R.id.entry_image_view_preview) as ImageView

        ///////////////////////////////
        // Get the dimensions of the View
        val targetW: Int = imageView.width
        val targetH: Int = imageView.height
        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true
            val photoW: Int = outWidth
            val photoH: Int = outHeight
            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)
            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            imageView.setImageBitmap(bitmap)
        }
    }

    override fun onPictureTaken(p0: ByteArray?, p1: Camera?) {
        TODO("Not yet implemented")
    }
}