package com.example.ecommercesalesapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.ecommercesalesapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DisplayProductDetailsActivity : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var productTitle : TextView
    lateinit var productPrice : TextView
    lateinit var productDesc : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_product_details)

        db = FirebaseFirestore.getInstance()
        //Initialise Firebase Auth
        auth = FirebaseAuth.getInstance()

        productTitle = findViewById(R.id.productTitle)
        productPrice = findViewById(R.id.productPrice)
        productDesc = findViewById(R.id.productDesc)

        val recyclerProductId = intent.getStringExtra("recyclerProductId")
        Log.d("!!!","In display product activity productId recvd:" + recyclerProductId)

        getProductDetails(recyclerProductId)
    }

    fun getProductDetails(productId:String?){
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