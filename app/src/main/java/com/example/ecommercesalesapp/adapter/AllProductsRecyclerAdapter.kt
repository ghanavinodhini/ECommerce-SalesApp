package com.example.ecommercesalesapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.allViews
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercesalesapp.R
import com.example.ecommercesalesapp.databinding.AllProductsBinding
import com.example.ecommercesalesapp.model.AllProducts
import com.example.ecommercesalesapp.viewModel.AllProductsViewModel

class AllProductsRecyclerAdapter(private val context: Context, private val productsArrayList : ArrayList<AllProductsViewModel>) : RecyclerView.Adapter<AllProductsRecyclerAdapter.AllProductsViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllProductsViewHolder
    {
      val layoutInflater = LayoutInflater.from(parent.context)

        val allProductsBinding : AllProductsBinding = DataBindingUtil.inflate(layoutInflater,R.layout.allproducts_list_view,parent,false)

        return AllProductsViewHolder(allProductsBinding)
        /*val view = LayoutInflater.from(context).inflate(R.layout.allproducts_list_view,parent,false)
        return AllProductsViewHolder(view)*/
    }

    override fun onBindViewHolder(holder: AllProductsViewHolder, position: Int) {
        val allProductsViewModel = productsArrayList[position]
        holder.bind(allProductsViewModel)
    }

    override fun getItemCount(): Int {
        Log.d("!!!","Inside recycler Item count: " + productsArrayList.size)
        return productsArrayList.size
    }

    //Create inner class
    inner class AllProductsViewHolder(val allProductsBinding: AllProductsBinding) : RecyclerView.ViewHolder(allProductsBinding.root)
    {
        /*val productTitle = itemView.findViewById<TextView>(R.id.allProducts_listView_titleTextView)
        val productPrice = itemView.findViewById<TextView>(R.id.allProducts_listView_priceTextView)
        //val productImage = itemView.findViewById<ImageView>(R.id.allProducts_listView_ImageView)*/

        fun bind(allProductsViewModel: AllProductsViewModel){
            /*productTitle.setText(productsDisplay.productTitle)
            productPrice.setText(productsDisplay.productPrice)*/
            this.allProductsBinding.allproductsmodel = allProductsViewModel
            allProductsBinding.executePendingBindings()
        }

    }



}