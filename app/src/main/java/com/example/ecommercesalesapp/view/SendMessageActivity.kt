package com.example.ecommercesalesapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.ecommercesalesapp.R
import com.example.ecommercesalesapp.model.AllProducts
import com.example.ecommercesalesapp.model.BuyerMessage
import com.example.ecommercesalesapp.viewModel.CreateAdvertisementViewModel
import com.example.ecommercesalesapp.viewModel.LoginRegisterViewModel
import com.example.ecommercesalesapp.viewModel.SendMessageViewModel
import java.util.*

class SendMessageActivity : AppCompatActivity() {
    lateinit var buyerBidPrice : EditText
    lateinit var buyerMessage : EditText
    lateinit var sendMessageBtn : Button
    lateinit var uid : String
    lateinit var buyerEmail : String
    lateinit var buyerInterestedProduct : String
    lateinit var buyerInterestedProductId : String
    lateinit var sellerId: String
    lateinit var loginRegisterViewModel: LoginRegisterViewModel
    lateinit var sendMessageViewModel: SendMessageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_message)

        buyerBidPrice = findViewById(R.id.buyerBidPrice)
        buyerMessage = findViewById(R.id.buyerMessage)
        sendMessageBtn = findViewById(R.id.buyerSendMessage)

        buyerInterestedProduct = intent.getStringExtra("productTitle").toString()
        buyerInterestedProductId = intent.getStringExtra("productId").toString()
        sellerId = intent.getStringExtra("sellerId").toString()

        loginRegisterViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)

        //Set Observer for Firebase User
        loginRegisterViewModel.getFirebaseUserMutableLiveData().observe(this, androidx.lifecycle.Observer {
            //Check if FirebaseUser not null
            if(it != null){
                uid = it.uid
                buyerEmail = it.email.toString()
            }
        })

        sendMessageViewModel = ViewModelProvider(this).get(SendMessageViewModel::class.java)

        sendMessageBtn.setOnClickListener {
            saveBuyerMessage()
            saveStatus()
        }
    }

    fun saveBuyerMessage(){
        val buyerMessage = BuyerMessage()
        buyerMessage.buyerInterestedProduct = buyerInterestedProduct
        buyerMessage.buyerInterestedProductId = buyerInterestedProductId
        buyerMessage.buyerBidPrice = this.buyerBidPrice.text.toString()
        buyerMessage.buyerMessage = this.buyerMessage.text.toString()
        buyerMessage.buyerUserId = uid
        buyerMessage.buyerEmail = buyerEmail
        buyerMessage.sellerId = this.sellerId

        buyerMessage.let { sendMessageViewModel.saveBuyerMessage(it) }
    }

    fun saveStatus(){
        sendMessageViewModel.addStatus.observe(this, androidx.lifecycle.Observer {
            if (it == true){
                Toast.makeText(this,"Message sent to owner successfully", Toast.LENGTH_SHORT).show()
                displayHomeActivity()
            }
            else{
                Toast.makeText(this,"Error in sending message", Toast.LENGTH_SHORT).show()
            }
        })
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