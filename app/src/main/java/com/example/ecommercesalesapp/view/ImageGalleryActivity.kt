package com.example.ecommercesalesapp.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.example.ecommercesalesapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ImageGalleryActivity : AppCompatActivity() {

    //Store URIs of selected images
     var gallerySelectedImages : ArrayList<Uri?>? = null
      var uriList : MutableList<String> = mutableListOf()

    //Current position/Index of selected images
    private var position = 0

    //Request Code to Select Image(s)
    private val PICK_IMAGES_CODE = 0

    lateinit var currentPhotoPath: String
    lateinit var storageRef: StorageReference
    lateinit var auth: FirebaseAuth
    lateinit var galleryImageSwitcher : ImageSwitcher
    lateinit var gallerySelectButton : Button
    lateinit var okGalleryButton : Button
    lateinit var previousButton : Button
    lateinit var nextButton: Button
    lateinit var advertisementId: String
    lateinit var f: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_gallery)

        galleryImageSwitcher = findViewById(R.id.galleryImageSwitcher)
        gallerySelectButton = findViewById(R.id.gallerySelectButton)
        okGalleryButton = findViewById(R.id.okGalleryButton)
        previousButton = findViewById(R.id.previousButton)
        nextButton = findViewById(R.id.nextButton)

        getIntentValue()
        buttonsVisibilitySwitch()

        //Initialise Firebase Auth
        auth = FirebaseAuth.getInstance()

        //Initialise firebase storage reference
        storageRef = FirebaseStorage.getInstance().getReference()

        //Initiate list
        gallerySelectedImages = ArrayList()

        //Setup ImageSwitcher
        galleryImageSwitcher.setFactory { ImageView(applicationContext) }

        //Select Gallery button
        gallerySelectButton.setOnClickListener {
            pickImagesIntent()
            okGalleryButton.isVisible = true
        }

        okGalleryButton.setOnClickListener {
            //saveGalleryReceiptsToFirebase()
           // val galleryImageFilePath = f.name + System.currentTimeMillis()
            saveGalleryImagesToFirebase(f.name,Uri.fromFile(f))
           // saveGalleryImagesToFirebase(galleryImageFilePath,Uri.fromFile(f))

        }

        nextButton.setOnClickListener {
            if(position < gallerySelectedImages!!.size - 1){
                position++
                galleryImageSwitcher.setImageURI(gallerySelectedImages!![position])
            }
            else{
                Toast.makeText(this,"No more images",Toast.LENGTH_SHORT).show()
                nextButton.isVisible = false
            }
        }

        previousButton.setOnClickListener {
            if(position > 0){
                position--
                galleryImageSwitcher.setImageURI(gallerySelectedImages!![position])
            }
            else{
                Toast.makeText(this,"No more images",Toast.LENGTH_SHORT).show()
                previousButton.isVisible = false
            }
        }

    }

    fun getIntentValue(){
        Log.d("!!!", "Inside getintent image gallery activity")
        advertisementId = intent.getStringExtra("productAdID").toString()
        Log.d("!!!", "Inside getintent image gallery activity: $advertisementId")
    }

    private fun buttonsVisibilitySwitch(){
        previousButton.isVisible = false
        nextButton.isVisible = false
        okGalleryButton.isVisible = false
    }

    private fun pickImagesIntent(){
        Log.d("!!!","Inside pickImages function")
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Select Image(s)"),PICK_IMAGES_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("!!!","Inside activity result function")
        if(requestCode == PICK_IMAGES_CODE){

            if(resultCode == Activity.RESULT_OK){

                if(data!!.clipData != null){
                    //Picked multiple images
                        //Enable Previous and Next buttons
                            previousButton.isVisible = true
                            nextButton.isVisible = true
                    //Get No. of picked images
                    var selCount = data.clipData!!.itemCount
                    for(i in 0 until selCount){
                        val selImageUri = data.clipData!!.getItemAt(i).uri
                        //Add selected image to arraylist
                        gallerySelectedImages?.add(selImageUri)
                        uriList.add(selImageUri.toString())//Add to String array

                    }

                    //Set 1st image from arraylist to ImageSwitcher
                    galleryImageSwitcher.setImageURI(gallerySelectedImages!![0])
                    position = 0


                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        Log.d("!!!", "Before calling createImageFile function")
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        Log.d("!!!", "Error occurred while creating file")
                        null
                    }
                    // Continue only if the File was successfully created
                  /*  photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.android.fileprovider",
                            it)
                        Log.d("!!!", "photoURI value: ${photoURI}")
                    }*/
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.ecommercesalesapp",
                            it)
                        Log.d("!!!", "photoURI value: ${photoURI}")
                    }
                    //val f = File(currentPhotoPath)
                    f = File(currentPhotoPath)
                    //saveGalleryImagesToFirebase(f.name,Uri.fromFile(f))--comented to save the gallery images list to Array variable


                }else{
                    //Picked Single image
                    val selImageUri = data.data

                    //Add selected image to arraylist
                    gallerySelectedImages?.add(selImageUri)
                    uriList.add(selImageUri.toString()) //Add to String array

                    //Set Image to ImageSwitcher
                    galleryImageSwitcher.setImageURI(selImageUri)
                    position = 0
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        Log.d("!!!", "Inside createImageFile function")
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

    private fun saveGalleryImagesToFirebase(imageFileName: String, fromFileUri: Uri) {
        Log.d("!!!","Inside Save gallery products to firebase function")
        Log.d("!!!","Inside Save gallery products : Size of array gallery:"+gallerySelectedImages!!.size)
        //Save in images collection "galleryimages/"
        //val uniqueId = UUID.randomUUID().toString()
        //val imageStorage = storageRef.child("{galleryimages/${imageFileName}-${uniqueId}}")
        for ( i in 0 until gallerySelectedImages!!.size){
            val uniqueId = UUID.randomUUID().toString()
            val imageStorage = storageRef.child("{galleryimages/${imageFileName}-${uniqueId}}")

            gallerySelectedImages!![i]?.let {
                Log.d("!!!", "Product Image URI from galleryimages Array: ${it.toString()}")
                imageStorage.putFile(it).addOnSuccessListener {
                    imageStorage.downloadUrl.addOnSuccessListener {
                        Log.d("!!!", "Product Image URI from firebase server: ${it.toString()}")
                        Toast.makeText(this, "Product Gallery Image saved to Firebase Storage", Toast.LENGTH_SHORT).show()

                        addImageUriToDatabase(it)
                    }
                }
            }
        }
    }

    private fun addImageUriToDatabase(uri: Uri){
        val db = FirebaseFirestore.getInstance()
        val galleryProductsData = HashMap<String, Any>()
        galleryProductsData["productGalleryImageUrl"] = uri.toString()
        galleryProductsData["productGalleryImageId"] = "Product_$uri"
        galleryProductsData["uid"] = auth.uid.toString()
        galleryProductsData["productId"] = advertisementId

        db.collection("images").document("${auth.uid.toString()}").collection("galleryProducts")
            .add(galleryProductsData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Saved Image Url to DB", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving Image Url to DB", Toast.LENGTH_LONG).show()
            }
        displayCreateAdvertisementActivity()
    }

    private fun displayCreateAdvertisementActivity(){
        val createAdIntent = Intent(this@ImageGalleryActivity,CreateAdvertisementActivity::class.java)
       // Log.d("!!!", "Inside displayCreateAdActivity uriList value to pass : $uriList")

        //createAdIntent.putExtra("gallerySelectedImagesList",uriList)
        createAdIntent.putExtra("galleryAdID",advertisementId )
        startActivity(createAdIntent)
        finish()
    }
}