package com.example.ecommercesalesapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercesalesapp.R

class CreateAdvertisementActivity : AppCompatActivity() {

    lateinit var postAdImagesRecyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_advertisement)

        postAdImagesRecyclerView = findViewById(R.id.postAdImagesRecyclerView)
        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Inflate toolbar menu
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }
}