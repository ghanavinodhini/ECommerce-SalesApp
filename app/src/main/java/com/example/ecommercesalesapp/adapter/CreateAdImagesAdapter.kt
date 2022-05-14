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

  /*  var createAdImagesList = emptyList<CreateAdImages>()


    internal fun setImagesDataList(imagesList: List<CreateAdImages>) {
        this.createAdImagesList = imagesList
    }*/

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CreateAdImagesAdapter.createAdImagesViewHolder {
        Log.d("!!!","Inside createAdImagesAdapter")
        var view = LayoutInflater.from(parent.context).inflate(R.layout.postad_productimages_view, parent, false)
        return createAdImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreateAdImagesAdapter.createAdImagesViewHolder, position: Int) {
        // Get the data model based on position
      /*  var data = createAdImagesList[position]

        //holder.image.setImageResource(data.image)
        holder.image.setImageBitmap(data.image)*/

        when(holder){
            is createAdImagesViewHolder->{holder.bind(listImages[position])}
        }
    }

    override fun getItemCount(): Int {
        return listImages.size
    }

    inner class createAdImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
       /* var image: ImageView

        init {
            image = itemView.findViewById(R.id.image)
        }*/
       val gridImage = itemView.findViewById<ImageView>(R.id.image)
        fun bind(imageDisplay:CreateAdImages){
            val uri = Uri.parse(imageDisplay.image)
           // gridImage.setImageURI(uri)
            Picasso.with(context).load(uri).into(gridImage)
        }
    }

}