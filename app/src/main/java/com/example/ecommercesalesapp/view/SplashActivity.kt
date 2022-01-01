package com.example.ecommercesalesapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.ecommercesalesapp.R
import com.google.android.material.button.MaterialButton

class SplashActivity : AppCompatActivity() {

    lateinit var startBtn : MaterialButton
    lateinit var splashText : TextView
    lateinit var splashImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Log.d("!!!","Inside Splash Activity")
        startBtn = findViewById(R.id.splashButton)
        splashImage = findViewById(R.id.splashImageView)
        splashText = findViewById(R.id.splashTextView)


        startBtn.setOnClickListener{
            clearFields()
            displayLoginRegistrationFragment()
        }
    }

    private fun clearFields(){
        startBtn.isVisible = false
        splashImage.isVisible = false
        splashText.isVisible = false
    }

    private fun displayLoginRegistrationFragment(){
        Log.d("!!!","Inside displayLoginRegFragment function")

        val bundle = Bundle()
        val profileType = "Login"
        bundle.putString("profiletype", profileType)
        val regFragment = LoginRegistrationFragment()
        regFragment.setArguments(bundle)

        val transaction = supportFragmentManager.beginTransaction()
        Log.d("!!!","After declaring fragment variables")
        transaction.add(R.id.mainSplashContainer,regFragment,"regfragment")
        transaction.commit()
    }
}