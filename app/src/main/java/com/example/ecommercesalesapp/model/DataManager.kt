package com.example.ecommercesalesapp.model

object DataManager {
    val allGridImages = mutableListOf<CreateAdImages>()

    init {
       // loadImageData()
    }

    fun loadImageData(){

        val i1 = CreateAdImages("content://com.example.ecommercesalesapp/external_files/Pictures/Product_20220508_153025_8155291853869541365.jpg")
        allGridImages.add(i1)
    }
}
