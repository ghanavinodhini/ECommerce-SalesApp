package com.example.ecommercesalesapp.model

class AllProducts {

        var productId = ""
        var productTitle = ""
        var productDescription = ""
        var productPrice = ""
        var productImageUri = ""
        var uid = ""

    constructor(productId:String,productTitle:String,desc:String,price:String,imagePath:String,uid:String){
        this.productId = productId
        this.productTitle = productTitle
        this.productDescription = desc
        this.productPrice = price
        this.productImageUri = imagePath
        this.uid = uid
    }


}