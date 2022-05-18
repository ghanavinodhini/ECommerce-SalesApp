package com.example.ecommercesalesapp.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercesalesapp.R
import com.example.ecommercesalesapp.databinding.PostadProductimagesViewBinding
import com.example.ecommercesalesapp.model.CreateAdImages
import com.example.ecommercesalesapp.view.ImageGalleryActivity
import com.squareup.picasso.Picasso

class CreateAdImagesAdapter(var context: Context, val listImages:List<CreateAdImages>) : RecyclerView.Adapter<CreateAdImagesAdapter.createAdImagesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CreateAdImagesAdapter.createAdImagesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.postad_productimages_view, parent, false)
        return createAdImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreateAdImagesAdapter.createAdImagesViewHolder, position: Int) {
        when(holder){
            is createAdImagesViewHolder->{holder.bind(listImages[position])}
        }
    }

    override fun getItemCount(): Int {
        return listImages.size
    }

    inner class createAdImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
       val gridImage = itemView.findViewById<ImageView>(R.id.image)

        fun bind(imageDisplay:CreateAdImages){
            val uri = Uri.parse(imageDisplay.image)
            Picasso.with(context).load(uri).into(gridImage)
        }
    }

}