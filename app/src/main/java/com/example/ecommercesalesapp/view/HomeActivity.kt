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
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercesalesapp.R
import com.example.ecommercesalesapp.adapter.AllProductsRecyclerAdapter
import com.example.ecommercesalesapp.adapter.onProductClickListener
import com.example.ecommercesalesapp.model.AllProducts
import com.example.ecommercesalesapp.model.DataManager
import com.example.ecommercesalesapp.viewModel.LoginRegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class HomeActivity : AppCompatActivity(),onProductClickListener {
    lateinit var userNameTextView:TextView
    lateinit var postAdButton : Button
    lateinit var messagesButton: ImageButton
    lateinit var currentUserName : String
    lateinit var loginRegisterViewModel: LoginRegisterViewModel
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var allProductsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("!!!","Inside home activity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        userNameTextView = findViewById(R.id.userNameTextView)
        postAdButton = findViewById(R.id.postAdbutton)
        messagesButton = findViewById(R.id.messagesButton)
        allProductsRecyclerView = findViewById(R.id.allProductsRecyclerView)


        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        loginRegisterViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)

        //Set Observer for Firebase User
        loginRegisterViewModel.getFirebaseUserMutableLiveData().observe(this, Observer<FirebaseUser>{
            //Check if FirebaseUser not null
            if(it != null){
                Log.d("!!!","Inside home activity loginRegisterviewmodel getfirebaselivedata")
                //currentUserName = loginRegisterViewModel.getUserName()
                userNameTextView.setText("WELCOME " + it.email?.uppercase(Locale.getDefault()))
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
        loadAllProducts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)

        val searchMenuItem = menu?.findItem(R.id.action_search)

        if (searchMenuItem != null){
            val searchView = searchMenuItem.actionView as SearchView

            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                   return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if(newText!!.isNotEmpty()){
                        DataManager.tempAllProducts.clear()
                        val search = newText.lowercase(Locale.getDefault())
                        DataManager.allProducts.forEach {
                            if(it.productTitle?.toLowerCase(Locale.getDefault())!!.contains(search)){
                                DataManager.tempAllProducts.add(it)
                            }
                        }
                        allProductsRecyclerView.adapter?.notifyDataSetChanged()
                    }
                    else{
                        DataManager.tempAllProducts.clear()
                        DataManager.tempAllProducts.addAll(DataManager.allProducts)
                        allProductsRecyclerView.adapter?.notifyDataSetChanged()
                    }
                   return true
                }

            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                loginRegisterViewModel.logout()
                true
            }
            else -> super.onOptionsItemSelected(item) //returns default false value
        }
    }

    override fun onProductClicked(position: Int, product: AllProducts) {
        val intent = Intent(this, DisplayProductDetailsActivity::class.java)
        intent.putExtra("recyclerProductId",product.productAdvertsementId)
        intent.putExtra("productOwnerId", product.uid)
        startActivity(intent)
    }

    fun loadAllProducts(){
      /*  allProductsRecyclerView.layoutManager = LinearLayoutManager(this)
        allProductsRecyclerView.adapter = AllProductsRecyclerAdapter(this, DataManager.allProducts,this)*/
       // allProductsRecyclerView.adapter = AllProductsRecyclerAdapter(this, DataManager.tempAllProducts,this)
        DataManager.tempAllProducts.clear()

        allProductsRecyclerView.adapter = AllProductsRecyclerAdapter(this, DataManager.tempAllProducts,this)
        allProductsRecyclerView.layoutManager = LinearLayoutManager(this)

        db.collection("products").get()
            .addOnSuccessListener {
                val productsSnapshotList = it.documents
                DataManager.allProducts.clear()

                for(snapshot in productsSnapshotList){
                    val product = AllProducts(
                        snapshot.getString("productId").toString(),
                        snapshot.getString("productTitle").toString(),
                        snapshot.getString("productDesc").toString(),
                        snapshot.getString("productPrice").toString(),
                        snapshot.getString("productImageUri").toString(),
                        snapshot.getString("userId").toString()
                    )
                    DataManager.allProducts.add(product)
                }
                allProductsRecyclerView.adapter?.notifyDataSetChanged()
                DataManager.tempAllProducts.addAll(DataManager.allProducts)
            }
       /* allProductsRecyclerView.adapter = AllProductsRecyclerAdapter(this, DataManager.tempAllProducts,this)
        allProductsRecyclerView.layoutManager = LinearLayoutManager(this)*/
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
        val intent = Intent(this,SplashActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        allProductsRecyclerView.adapter?.notifyDataSetChanged()
    }
}