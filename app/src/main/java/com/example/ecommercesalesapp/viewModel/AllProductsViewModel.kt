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
        //Here fetch from DB code has to implemented...
       /* val allProduct1 = AllProducts("1","Product1","This is First product","100","image1.jpg","1234")
        val allProduct2 = AllProducts("2","Product2","This is Second product","200","image2.jpg","1234")
        val allProduct3 = AllProducts("3","Product3","This is Third product","300","image3.jpg","7896")
        val allProduct4 = AllProducts("4","Product4","This is Fourth product","400","image4.jpg","2678")

        val allProductViewModel1 : AllProductsViewModel = AllProductsViewModel(allProduct1)
        val allProductViewModel2 : AllProductsViewModel = AllProductsViewModel(allProduct2)
        val allProductViewModel3 : AllProductsViewModel = AllProductsViewModel(allProduct3)
        val allProductViewModel4 : AllProductsViewModel = AllProductsViewModel(allProduct4)

        allProductsList.add(allProductViewModel1)
        allProductsList.add(allProductViewModel2)
        allProductsList.add(allProductViewModel3)
        allProductsList.add(allProductViewModel4)*/

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

