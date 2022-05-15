package com.example.ecommercesalesapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercesalesapp.R
import com.example.ecommercesalesapp.model.BuyerMessage

class AllMessagesRecyclerAdapter(var context: Context, val messagesList:List<BuyerMessage>) : RecyclerView.Adapter<AllMessagesRecyclerAdapter.messagesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllMessagesRecyclerAdapter.messagesViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.messages_list_view,parent,false)
        return messagesViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AllMessagesRecyclerAdapter.messagesViewHolder,
        position: Int
    ) {
        when(holder){
            is AllMessagesRecyclerAdapter.messagesViewHolder ->{holder.bind(messagesList[position])}
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    inner class messagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var buyerProductTitle = itemView.findViewById<TextView>(R.id.allMessages_productTitle)
        val buyerProductPrice = itemView.findViewById<TextView>(R.id.allMessages_productPrice)
        val buyerProductMessage = itemView.findViewById<TextView>(R.id.allMessages_buyerMessage)

        fun bind(messagesDisplay:BuyerMessage){
            buyerProductTitle.setText(messagesDisplay.buyerInterestedProduct)
            buyerProductPrice.setText(messagesDisplay.buyerBidPrice)
            buyerProductMessage.setText(messagesDisplay.buyerMessage)
        }
    }
}