package com.example.ecommercesalesapp.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.ecommercesalesapp.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class DisplayProductDetailsActivity : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var productTitle : TextView
    lateinit var productPrice : TextView
    lateinit var productDesc : TextView
    lateinit var productId: String
     var productOwnerId: String? = null
    lateinit var productUserId: String
    lateinit var bidButton : Button
    lateinit var okButton: Button
    lateinit var imageCarousel : CarouselView
     var productImagesUri:MutableList<Uri> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_product_details)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        productTitle = findViewById(R.id.productTitle)
        productPrice = findViewById(R.id.productPrice)
        productDesc = findViewById(R.id.productDesc)
        bidButton = findViewById(R.id.bidButton)
        okButton = findViewById(R.id.okButton)
        imageCarousel = findViewById(R.id.imageCarouselView)

        productId = intent.getStringExtra("recyclerProductId").toString()
        productUserId = intent.getStringExtra("productOwnerId").toString()

        getProductImages(productId)
        getProductDetails(productId)

        bidButtonVisibility()

        bidButton.setOnClickListener {
           displaySendMessageActivity()
        }

        okButton.setOnClickListener {
            displayHomeActivity()
        }
    }

    fun bidButtonVisibility(){
        Log.d("!!!","current user uid: " + auth.currentUser?.uid.toString())
        Log.d("!!!","product owner id in button visibility: " + productUserId)
        if(auth.currentUser?.uid.toString() == productUserId){
            bidButton.isVisible = false
            okButton.isVisible = true
        }
        else{
            bidButton.isVisible = true
            okButton.isVisible = false
        }
    }

    fun getProductImages(productId: String?){
        db.collection("images").whereEqualTo("productId",productId).get()
            .addOnSuccessListener {
                val snapshotList = it.documents
                for(snapshot in snapshotList) {
                    val snapshotUri = snapshot.get("productGalleryImageUrl").toString()
                    productImagesUri.add(Uri.parse(snapshotUri))
                }
                loadProductImages(productImagesUri)
            }
            .addOnFailureListener {
                Log.d("!!!", "Exception in snapshot: ${it.message}")
            }
    }

    fun loadProductImages(productImagesUri: MutableList<Uri>) {
        imageCarousel.setImageListener(imageListener)
        imageCarousel.pageCount = productImagesUri.size
    }

    var imageListener: ImageListener = ImageListener { position, imageView ->
        Picasso.with(applicationContext).load(productImagesUri[position]).into(imageView)
    }

    fun getProductDetails(productId:String?){
        db.collection("products").whereEqualTo("productId",productId).get()
            .addOnSuccessListener {
                val snapshotList = it.documents
                for(snapshot in snapshotList) {
                    val productTitle = snapshot.get("productTitle").toString()
                    val productPrice = snapshot.get("productPrice").toString()
                    val productDesc = snapshot.get("productDesc").toString()
                    productOwnerId = snapshot.get("userId").toString()

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
    }

    fun displaySendMessageActivity(){
        val intent = Intent(this,SendMessageActivity::class.java)
        intent.putExtra("productTitle",this.productTitle.text)
        intent.putExtra("productId",this.productId)
        intent.putExtra("sellerId",this.productOwnerId)
        startActivity(intent)
        finish()
    }

    fun displayHomeActivity(){
        val intent = Intent(this,HomeActivity::class.java)
        this.startActivity(intent)
        this.finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,HomeActivity::class.java)
        this.startActivity(intent)
        this.finish()
    }

}