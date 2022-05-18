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
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercesalesapp.R
import com.example.ecommercesalesapp.adapter.CreateAdImagesAdapter
import com.example.ecommercesalesapp.model.AllProducts
import com.example.ecommercesalesapp.model.CreateAdImages
import com.example.ecommercesalesapp.model.DataManager
import com.example.ecommercesalesapp.viewModel.CreateAdvertisementViewModel
import com.example.ecommercesalesapp.viewModel.LoginRegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*

class CreateAdvertisementActivity : AppCompatActivity() {
    lateinit var postAdImagesRecyclerView : RecyclerView
    lateinit var newAdNextButton : Button
    lateinit var okButton : Button
    lateinit var productTitle : TextView
    lateinit var productPrice : TextView
    lateinit var productDesc : TextView
    lateinit var uid : String
    lateinit var newAdvertisementId: String
    lateinit var galleryAdvertisementId: String
    lateinit var mainImageUrlId: String
    lateinit var loginRegisterViewModel: LoginRegisterViewModel
    lateinit var createAdvertisementViewModel: CreateAdvertisementViewModel
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_advertisement)

        postAdImagesRecyclerView = findViewById(R.id.postAdImagesRecyclerView)
        newAdNextButton = findViewById(R.id.postNewAdNextButton)
        okButton = findViewById(R.id.okButton)
        productTitle = findViewById(R.id.editTextNewProductTitle)
        productPrice = findViewById(R.id.editTextNewProductPrice)
        productDesc = findViewById(R.id.editTextNewProductDesc)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

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

        //Save ad details to DB
        newAdNextButton.setOnClickListener {
            saveNewProduct()
            saveStatus()
        }

        okButton.setOnClickListener {
            displayHomeActivity()
        }

    }

    fun getIntentValue(){
        galleryAdvertisementId = intent.getStringExtra("galleryAdID").toString()
        if(galleryAdvertisementId != ""){
            loadGridImageData()
            getProductDetails()
        }
    }

    fun loadGridImageData(){
        postAdImagesRecyclerView.layoutManager = GridLayoutManager(applicationContext,2)
        postAdImagesRecyclerView.adapter = CreateAdImagesAdapter(this,DataManager.allGridImages)

        db.collection("images").whereEqualTo("productId",galleryAdvertisementId).get()
            .addOnSuccessListener {
                val snapshotList = it.documents
                DataManager.allGridImages.clear()
                mainImageUrlId = snapshotList[0].get("productGalleryImageUrl").toString()
                for(snapshot in snapshotList) {
                    val snapshotUri = snapshot.get("productGalleryImageUrl").toString()
                    DataManager.allGridImages.add(CreateAdImages(snapshotUri))
                }
                /* To update field */
                updateImageUrlField()
            }
            .addOnFailureListener {
                Log.d("!!!", "Exception in snapshot: ${it.message}")
            }
        postAdImagesRecyclerView.adapter?.notifyDataSetChanged()
    }

    fun updateImageUrlField(){
        val ref = db.collection("products")
        ref.whereEqualTo("productId",galleryAdvertisementId).get().addOnCompleteListener {
            if(it.isSuccessful){
                for(doc in it.result!!){
                    val uriUpdate: MutableMap<String, Any> = HashMap()
                    uriUpdate["productImageUri"] = mainImageUrlId
                    ref.document(doc.id).set(uriUpdate, SetOptions.merge())
                }
            }
        }
    }

    fun getProductDetails(){
        db.collection("products").whereEqualTo("productId",galleryAdvertisementId).get()
            .addOnSuccessListener {
                val snapshotList = it.documents
                for(snapshot in snapshotList) {
                    val productTitle = snapshot.get("productTitle").toString()
                    val productPrice = snapshot.get("productPrice").toString()
                    val productDesc = snapshot.get("productDesc").toString()

                    displayProductDetails(productTitle,productPrice,productDesc)
                }
            }
            .addOnFailureListener {
                Log.d("!!!", "Exception in snapshot: ${it.message}")
            }
    }

    fun displayProductDetails(productTitle:String, productPrice:String, productDesc:String){
        this.productTitle.text = productTitle
        this.productPrice.text = productPrice
        this.productDesc.text = productDesc

        this.productTitle.isEnabled = false
        this.productPrice.isEnabled = false
        this.productDesc.isEnabled = false

        okButton.isVisible = true
        newAdNextButton.isVisible = false
    }

    fun saveNewProduct(){
        val newProduct = AllProducts()
        newProduct.productAdvertsementId = UUID.randomUUID().toString()
        newAdvertisementId = newProduct.productAdvertsementId.toString()
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
                //Toast.makeText(this,"New Product saved successfully",Toast.LENGTH_SHORT).show()
                displayImageGalleryActivity()
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

    fun displayImageGalleryActivity(){
        val intent = Intent(this, ImageGalleryActivity::class.java)
        intent.putExtra("productAdID",newAdvertisementId )
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