package com.example.ecommercesalesapp.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.ecommercesalesapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class ImageCaptureDisplayActivity : AppCompatActivity() {
    lateinit var currentPhotoPath: String
    val CAMERA_REQUEST_CODE = 99
    lateinit var storageRef: StorageReference
    lateinit var auth:FirebaseAuth
    lateinit var capturedProductImageView: ImageView
    lateinit var contentUri:Uri
    lateinit var photoURI:Uri
    lateinit var advertisementId: String

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_capture_display)

        auth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().getReference()

        capturedProductImageView = findViewById(R.id.capturedProductImageView)
        val okButton = findViewById<Button>(R.id.okButton)

        getIntentValue()
        dispatchTakePictureIntent()

        okButton.setOnClickListener {
            displayGalleryActivity()
        }
    }

    fun getIntentValue(){
        advertisementId = intent.getStringExtra("galleryAdID").toString()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.d("!!!", "Error occurred while creating file")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                        this,
                        "com.example.ecommercesalesapp",
                        it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        //val storageDir:File? = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Product_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
            val f = File(currentPhotoPath)
            capturedProductImageView.setImageURI(Uri.fromFile(f))

            try {
                val imageUri = galleryAddPic()//Add image to Gallery

                Toast.makeText(this, "Image saved to Gallery", Toast.LENGTH_SHORT).show()

                //Save Image  to Firebase Storage
                saveImageToFirebase(f.name, imageUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun galleryAddPic(): Uri {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            contentUri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            sendBroadcast(mediaScanIntent)
            return contentUri
        }
    }

    private fun saveImageToFirebase(imageName: String, imageUri: Uri) {
        val uniqueId = UUID.randomUUID().toString()
        val imageStorage =
            storageRef.child("{images/${imageName}-${uniqueId}}") //Save in images collection "images/ imageNAME.JPG"
        imageStorage.putFile(imageUri).addOnSuccessListener {
            imageStorage.downloadUrl.addOnSuccessListener {
                Toast.makeText(this, "Image saved to Firebase Storage", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun displayGalleryActivity(){
        val intent = Intent(this, ImageGalleryActivity::class.java)
        intent.putExtra("productAdID",advertisementId )
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,HomeActivity::class.java)
        this.startActivity(intent)
        this.finish()
    }
}


