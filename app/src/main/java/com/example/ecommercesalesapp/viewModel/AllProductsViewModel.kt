package com.example.ecommercesalesapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecommercesalesapp.model.AllProducts
import com.google.firebase.firestore.FirebaseFirestore

class AllProductsViewModel : ViewModel {

    var productId = ""
    var productTitle = ""
    var productDescription = ""
    var productPrice = ""
    var productImageUri = ""
    var uid = ""

     var db: FirebaseFirestore

     constructor() : super()
     constructor(allProducts: AllProducts) : super()
    {
        Log.d("!!!", "Inside AllProductsViewModel constructor ")
        this.productId = allProducts.productAdvertsementId.toString()
        this.productTitle = allProducts.productTitle.toString()
        this.productDescription = allProducts.productDescription.toString()
        this.productPrice = allProducts.productPrice.toString()
        this.productImageUri = allProducts.productImageUri.toString()
        this.uid = allProducts.uid.toString()
    }

    init {
        db = FirebaseFirestore.getInstance()

    }

    var allProductListMutableLiveData = MutableLiveData<ArrayList<AllProductsViewModel>>()
    var allProductsList = ArrayList<AllProductsViewModel>()


   /* fun getAllProductsImageUrl() : String{
        return productImageUri
    }*/

   fun getAllProductsListMutableLiveData() : MutableLiveData<ArrayList<AllProductsViewModel>>
    {
        db.collection("products").get()
            .addOnSuccessListener {
                for (doc in it){
                    val allProducts = AllProducts()
                    allProducts.uid = doc.getString("userId")
                    allProducts.productAdvertsementId = doc.getString("productId")
                    allProducts.productTitle = doc.getString("productTitle")
                    allProducts.productPrice = doc.getString("productPrice")
                    allProducts.productDescription = doc.getString("productDesc")
                    allProducts.productImageUri = doc.getString("productImageUri")

                    val allProductsViewModel = AllProductsViewModel(allProducts)
                    allProductsList.add(allProductsViewModel)
                    Log.d("!!!", "All Products List inside getAllProducts in adapter: $allProductsList")

                }
            }
      /*  val allProduct1 = AllProducts()
        val allProductViewModel1: AllProductsViewModel = AllProductsViewModel(allProduct1)
        allProductsList.add(allProductViewModel1)*/

        allProductListMutableLiveData.value = allProductsList
        return allProductListMutableLiveData
    }
}

