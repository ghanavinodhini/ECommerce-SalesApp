package com.example.ecommercesalesapp.model

data class BuyerMessage (
    var buyerInterestedProduct : String? = null,
    var buyerInterestedProductId : String? = null,
    var buyerBidPrice : String? = null,
    var buyerMessage : String? = null,
    var buyerUserId : String? = null,
    var buyerEmail : String? = null,
    var sellerId : String? = null
)