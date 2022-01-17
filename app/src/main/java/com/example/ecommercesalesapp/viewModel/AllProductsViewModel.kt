package com.example.ecommercesalesapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecommercesalesapp.model.AllProducts
import com.example.ecommercesalesapp.model.AuthRepository
import com.google.firebase.auth.FirebaseUser

class AllProductsViewModel : ViewModel {

    var productId = ""
    var productTitle = ""
    var productDescription = ""
    var productPrice = ""
    var productImageUri = ""
    var uid = ""

    constructor() : super()
    constructor(allProducts: AllProducts) : super()
    {
        this.productId = allProducts.productId
        this.productTitle = allProducts.productTitle
        this.productDescription = allProducts.productDescription
        this.productPrice = allProducts.productPrice
        this.productImageUri = allProducts.productImageUri
        this.uid = allProducts.uid
    }

   /* init {
        populateAllProductsList()
    }*/

    var allProductListMutableLiveData = MutableLiveData<ArrayList<AllProductsViewModel>>()

    var allProductsList = ArrayList<AllProductsViewModel>()

    fun getAllProductsImageUrl() : String{
        return productImageUri
    }

   /* fun populateAllProductsList(){
        //Here fetch from DB code has to implemented...
        val allProduct1 = AllProducts("1","Product1","This is First product","100","image1.jpg","1234")
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
        allProductsList.add(allProductViewModel4)

        allProductListMutableLiveData.value = allProductsList
    }*/

    fun getAllProductsListMutableLiveData() : MutableLiveData<ArrayList<AllProductsViewModel>>
    {
        //Here fetch from DB code has to implemented...
        val allProduct1 = AllProducts("1","Product1","This is First product","100","image1.jpg","1234")
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
        allProductsList.add(allProductViewModel4)

        allProductListMutableLiveData.value = allProductsList

        return allProductListMutableLiveData
    }
}