package com.example.ecommercesalesapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecommercesalesapp.model.BuyerMessage
import com.google.firebase.firestore.FirebaseFirestore

class SendMessageViewModel : ViewModel() {
    var db: FirebaseFirestore
    var addStatus = MutableLiveData<Boolean?>()

    init {
        db = FirebaseFirestore.getInstance()
    }

    fun saveBuyerMessage(message:BuyerMessage){
        val buyerMessageDetails = hashMapOf("buyerInterestedProduct" to message.buyerInterestedProduct,"buyerInterestedProductId" to message.buyerInterestedProductId, "buyerBidPrice" to message.buyerBidPrice,"buyerMessage" to message.buyerMessage,
            "buyerUserId" to message.buyerUserId,"buyerEmail" to message.buyerEmail, "sellerId" to message.sellerId)

        message.buyerUserId?.let {
            db.collection("messages")
                .add(buyerMessageDetails)
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