package com.example.ecommercesalesapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercesalesapp.R
import com.example.ecommercesalesapp.model.AllProducts
import com.squareup.picasso.Picasso

class AllProductsRecyclerAdapter(private val context: Context, private val productsArrayList : List<AllProducts>,private val productListener: onProductClickListener) : RecyclerView.Adapter<AllProductsRecyclerAdapter.AllProductsViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllProductsViewHolder
    {
        val view = LayoutInflater.from(context).inflate(R.layout.allproducts_list_view,parent,false)
        return AllProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllProductsViewHolder, position: Int) {
        when(holder){
            is AllProductsRecyclerAdapter.AllProductsViewHolder ->{holder.bind(productsArrayList[position])}
        }

        holder.itemView.setOnClickListener {
            productListener.onProductClicked(position,productsArrayList[position])
        }
    }

    override fun getItemCount(): Int {
        return productsArrayList.size
    }

    inner class AllProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var allProductTitle = itemView.findViewById<TextView>(R.id.allProducts_listView_titleTextView)
        var allProductPrice = itemView.findViewById<TextView>(R.id.allProducts_listView_priceTextView)
        var allProductImage = itemView.findViewById<ImageView>(R.id.allProducts_listView_ImageView)

        fun bind(allProductDisplay:AllProducts){
            allProductTitle.setText(allProductDisplay.productTitle)
            allProductPrice.setText(allProductDisplay.productPrice)
            val allProductImageUri = Uri.parse(allProductDisplay.productImageUri)
            Picasso.with(context).load(allProductImageUri).into(allProductImage)
        }
    }
}

interface onProductClickListener{
    fun onProductClicked(position: Int, product: AllProducts)
}