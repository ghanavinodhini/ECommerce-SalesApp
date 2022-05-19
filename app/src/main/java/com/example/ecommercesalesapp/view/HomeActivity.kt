package com.example.ecommercesalesapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercesalesapp.R
import com.example.ecommercesalesapp.adapter.AllProductsRecyclerAdapter
import com.example.ecommercesalesapp.adapter.onProductClickListener
import com.example.ecommercesalesapp.model.AllProducts
import com.example.ecommercesalesapp.model.DataManager
import com.example.ecommercesalesapp.viewModel.AllProductsViewModel
import com.example.ecommercesalesapp.viewModel.LoginRegisterViewModel
import com.google.firebase.auth.FirebaseUser

class HomeActivity : AppCompatActivity(),onProductClickListener {
    lateinit var userNameTextView:TextView
    lateinit var postAdButton : Button
    lateinit var messagesButton: ImageButton
    lateinit var currentUserName : String
    lateinit var loginRegisterViewModel: LoginRegisterViewModel
    lateinit var allProductsViewModel: AllProductsViewModel
    var allProducts = MutableLiveData<ArrayList<AllProducts>>()
    lateinit var allProductsRecyclerView: RecyclerView
    lateinit var allProductsRecyclerAdapter: AllProductsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        userNameTextView = findViewById(R.id.userNameTextView)
        postAdButton = findViewById(R.id.postAdbutton)
        messagesButton = findViewById(R.id.messagesButton)
        allProductsRecyclerView = findViewById(R.id.allProductsRecyclerView)


        loginRegisterViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)

        //Set Observer for Firebase User
        loginRegisterViewModel.getFirebaseUserMutableLiveData().observe(this, Observer<FirebaseUser>{
            //Check if FirebaseUser not null
            if(it != null){
                //currentUserName = loginRegisterViewModel.getUserName()
                userNameTextView.setText("WELCOME " + it.email?.toUpperCase())
                //userNameTextView.setText("WELCOME " + currentUserName.toUpperCase())
            }
        })

        loginRegisterViewModel.getUserLoggedStatus().observe(this, Observer<Boolean>{
            //Check if FirebaseUser loggedOut is true
            if(it){
               displaySplashScreen()
            }
        })

        postAdButton.setOnClickListener {
            displayCreateAdvertisement()
        }

        messagesButton.setOnClickListener {
            displayMessages()
        }

       allProductsViewModel = ViewModelProvider(this).get(AllProductsViewModel::class.java)

        //Set Observer for allProductsList
        allProductsViewModel.getAllProductsListMutableLiveData().observe(this, Observer {allProductsViewModel->

            allProductsRecyclerAdapter = AllProductsRecyclerAdapter(this@HomeActivity,allProductsViewModel!!,this)
            allProductsRecyclerView.setLayoutManager(LinearLayoutManager(this@HomeActivity))
            allProductsRecyclerView.setAdapter(allProductsRecyclerAdapter)
        }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Inflate toolbar menu
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    // Implement camera icon click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                loginRegisterViewModel.logout()
                true
            }
            else -> super.onOptionsItemSelected(item) //returns default false value
        }
    }

    override fun onProductClicked(position: Int, product: AllProductsViewModel) {
        val intent = Intent(this, DisplayProductDetailsActivity::class.java)
        intent.putExtra("recyclerProductId",product.productId)
        intent.putExtra("productOwnerId", product.uid)
        startActivity(intent)
    }


    private fun displayCreateAdvertisement(){
        val createAdIntent = Intent(this,CreateAdvertisementActivity::class.java)
          val dummyIntentList = arrayListOf("list items here")
        createAdIntent.putExtra("gallerySelectedImagesList", dummyIntentList)
        createAdIntent.putExtra("galleryAdID","" )
        startActivity(createAdIntent)
        finish()
    }

    private fun displayMessages(){
        val intent = Intent(this,DisplayMessagesActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun displaySplashScreen(){
        //Call new Activity from fragment
        val intent = Intent(this,SplashActivity::class.java)
        startActivity(intent)
        finish()

    }


}