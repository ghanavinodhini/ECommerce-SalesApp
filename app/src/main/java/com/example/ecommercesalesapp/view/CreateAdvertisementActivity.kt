package com.example.ecommercesalesapp.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercesalesapp.R
import com.example.ecommercesalesapp.model.AllProducts
import com.example.ecommercesalesapp.viewModel.CreateAdvertisementViewModel
import com.example.ecommercesalesapp.viewModel.LoginRegisterViewModel
import java.util.*
import kotlin.collections.ArrayList

class CreateAdvertisementActivity : AppCompatActivity() {

    lateinit var postAdImagesRecyclerView : RecyclerView
    lateinit var saveAdButton : Button
    lateinit var productTitle : TextView
    lateinit var productPrice : TextView
    lateinit var productDesc : TextView
    lateinit var uid : String
    // lateinit var galleryUriList : ArrayList<String>
    lateinit var loginRegisterViewModel: LoginRegisterViewModel
    lateinit var createAdvertisementViewModel: CreateAdvertisementViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_advertisement)

        postAdImagesRecyclerView = findViewById(R.id.postAdImagesRecyclerView)
        saveAdButton = findViewById(R.id.postNewAdButton)
        productTitle = findViewById(R.id.editTextNewProductTitle)
        productPrice = findViewById(R.id.editTextNewProductPrice)
        productDesc = findViewById(R.id.editTextNewProductDesc)

        getIntentValue()

        createAdvertisementViewModel = ViewModelProvider(this).get(CreateAdvertisementViewModel::class.java)
        loginRegisterViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)

        //Set Observer for Firebase User
        loginRegisterViewModel.getFirebaseUserMutableLiveData().observe(this, androidx.lifecycle.Observer {
            //Check if FirebaseUser not null
            if(it != null){
               uid = it.uid
            }
        })

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Save ad details to DB
        saveAdButton.setOnClickListener {
            saveNewProduct()
            saveStatus()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Inflate toolbar menu
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    // Implement camera icon click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_camera -> {
                Log.d("!!!", "Inside camera menu item option selected ")
                displayImageCaptureDisplayActivity()
                true
            }
            R.id.action_gallery -> {
                Log.d("!!!", "Inside gallery menu item option selected ")
                displayImageGalleryActivity()
                true
            }
            else -> super.onOptionsItemSelected(item) //returns default false value
        }
    }

    fun getIntentValue(){
        Log.d("!!!", "Inside getintent create ad activity")
        val galleryUriList = intent.getSerializableExtra("gallerySelectedImagesList")
        Log.d("!!!", "get intent value uri string list : $galleryUriList")
    }

    fun saveNewProduct(){
        val newProduct = AllProducts()
        newProduct.productAdvertsementId = UUID.randomUUID().toString()
        newProduct.productTitle = productTitle.text.toString()
        newProduct.productPrice = productPrice.text.toString()
        newProduct.productDescription = productDesc.text.toString()
        newProduct.productImageUri = "image.jpg"
        newProduct.uid = uid

        newProduct.let { createAdvertisementViewModel.createNewProduct(it) }
    }

    fun saveStatus(){
        createAdvertisementViewModel.addStatus.observe(this, androidx.lifecycle.Observer {
            if (it == true){
                Toast.makeText(this,"New Product saved successfully",Toast.LENGTH_SHORT).show()
                displayHomeActivity()
            }
            else{
                Toast.makeText(this,"Error in saving new product",Toast.LENGTH_SHORT).show()
            }
        } )
    }

    fun displayHomeActivity(){
        val intent = Intent(this,HomeActivity::class.java)
        this.startActivity(intent)
        this.finish()
    }

    fun displayImageCaptureDisplayActivity(){
        val intent = Intent(this,ImageCaptureDisplayActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun displayImageGalleryActivity(){
        val intent = Intent(this, ImageGalleryActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,HomeActivity::class.java)
        this.startActivity(intent)
        this.finish()
    }
}