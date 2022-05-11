package com.example.ecommercesalesapp.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.ecommercesalesapp.R
import com.squareup.picasso.Picasso

object ImageBindingAdapter {
    @JvmStatic
    @BindingAdapter("android:src")
    fun setAllProductsImageUrl(allproductsImageview:ImageView,imageUrl:String)
    {
        Picasso.with(allproductsImageview.context)
            .load(imageUrl).placeholder(R.drawable.splashimage).fit().into(allproductsImageview)
    }
}