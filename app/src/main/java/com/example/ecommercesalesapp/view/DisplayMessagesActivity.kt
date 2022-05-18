package com.example.ecommercesalesapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercesalesapp.R
import com.example.ecommercesalesapp.adapter.AllMessagesRecyclerAdapter
import com.example.ecommercesalesapp.model.BuyerMessage
import com.example.ecommercesalesapp.model.DataManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DisplayMessagesActivity : AppCompatActivity() {
    lateinit var messagesRecyclerView: RecyclerView
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_messages)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView)
        loadMessages()
    }

    fun loadMessages(){
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.adapter = AllMessagesRecyclerAdapter(this, DataManager.allMessages)

        db.collection("messages").whereEqualTo("sellerId",(auth.currentUser?.uid)).get()
            .addOnSuccessListener {
                val snapshotList = it.documents
                DataManager.allMessages.clear()

                    for(snapshot in snapshotList){
                        val message = BuyerMessage(
                            snapshot.getString("buyerInterestedProduct").toString(),
                            snapshot.getString("buyerInterestedProductId").toString(),
                            snapshot.getString("buyerBidPrice").toString(),
                            snapshot.getString("buyerMessage").toString(),
                            snapshot.getString("buyerUserId").toString(),
                            snapshot.getString("buyerEmail").toString(),
                            snapshot.get("sellerId").toString()
                        )
                        DataManager.allMessages.add(message)
                    }
                messagesRecyclerView.adapter?.notifyDataSetChanged()
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,HomeActivity::class.java)
        this.startActivity(intent)
        this.finish()
    }
}