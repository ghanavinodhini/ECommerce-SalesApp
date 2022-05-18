package com.example.ecommercesalesapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecommercesalesapp.model.AllProducts
import com.google.firebase.firestore.FirebaseFirestore

class CreateAdvertisementViewModel: ViewModel() {
    var db: FirebaseFirestore
    var addStatus = MutableLiveData<Boolean?>()

    init {
        db = FirebaseFirestore.getInstance()
    }


    fun createNewProduct(product : AllProducts)
    {
    val newProductDetails = hashMapOf("productId" to product.productAdvertsementId,"productTitle" to product.productTitle,"productPrice" to product.productPrice,
        "productDesc" to product.productDescription,"productImageUri" to product.productImageUri,"userId" to product.uid)

        product.uid?.let {
            db.collection("products")
            .add(newProductDetails)
            .addOnSuccessListener {
                    documentReference ->
                addStatus.value = true
            }
            .addOnFailureListener {
                addStatus.value = false
            }
        }
}
}