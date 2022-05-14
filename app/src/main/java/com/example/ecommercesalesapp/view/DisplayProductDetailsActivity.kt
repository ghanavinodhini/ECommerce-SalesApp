package com.example.ecommercesalesapp.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.ecommercesalesapp.R
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
    lateinit var imageCarousel : CarouselView
     var productImagesUri:MutableList<Uri> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_product_details)

        db = FirebaseFirestore.getInstance()
        //Initialise Firebase Auth
        auth = FirebaseAuth.getInstance()

        productTitle = findViewById(R.id.productTitle)
        productPrice = findViewById(R.id.productPrice)
        productDesc = findViewById(R.id.productDesc)
        imageCarousel = findViewById(R.id.imageCarouselView)

        val recyclerProductId = intent.getStringExtra("recyclerProductId")
        Log.d("!!!","In display product activity productId recvd:" + recyclerProductId)

        getProductImages(recyclerProductId)
        getProductDetails(recyclerProductId)
    }

    fun getProductImages(productId: String?){
        Log.d("!!!", "Inside getProduct Images displayProduct activity")


        db.collection("images").document("${auth.uid.toString()}").collection("galleryProducts").whereEqualTo("productId",productId).get()
            .addOnSuccessListener {
                val snapshotList = it.documents

                for(snapshot in snapshotList) {
                    val snapshotUri = snapshot.get("productGalleryImageUrl").toString()
                    productImagesUri.add(Uri.parse(snapshotUri))
                }
                Log.d("!!!", "Product Images Uri in get Product Images: ${productImagesUri}")
                loadProductImages(productImagesUri)
            }
            .addOnFailureListener {
                Log.d("!!!", "Exception in snapshot: ${it.message}")
            }
    }

    fun loadProductImages(productImagesUri: MutableList<Uri>) {
        Log.d("!!!", "Inside loadProductImages displayProduct Activity")


        imageCarousel.setImageListener(imageListener)
        imageCarousel.pageCount = productImagesUri.size
        Log.d("!!!", "Inside loadProductImages imagecarousel count: " + imageCarousel.pageCount)

    }

    var imageListener: ImageListener = ImageListener { position, imageView -> // You can use Glide or Picasso here
        //imageView.setImageResource(sampleImages[position])
        Log.d("!!!", "Inside imagelistener: " + productImagesUri[position])
        Picasso.with(applicationContext).load(productImagesUri[position]).into(imageView)
    }

    fun getProductDetails(productId:String?){
        Log.d("!!!", "Inside get Product Details displayProduct Activity")
        db.collection("products").whereEqualTo("productId",productId).get()
            .addOnSuccessListener {
                Log.d("!!!", "Iside successlisener")
                val snapshotList = it.documents
                Log.d("!!!", "Snapshot List: $snapshotList")
                for(snapshot in snapshotList) {
                    Log.d("!!!", "Products data: " + snapshot.getData())
                    val productTitle = snapshot.get("productTitle").toString()
                    val productPrice = snapshot.get("productPrice").toString()
                    val productDesc = snapshot.get("productDesc").toString()
                    Log.d("!!!", "Product Title: $productTitle, Product Price: $productPrice, Product Desc: $productDesc")
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,HomeActivity::class.java)
        this.startActivity(intent)
        this.finish()
    }

}