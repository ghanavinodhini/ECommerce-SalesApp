package com.example.ecommercesalesapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.ecommercesalesapp.model.AuthRepository
import com.google.firebase.auth.FirebaseUser

class LoginRegisterViewModel(application: Application) : AndroidViewModel(application) {

    private  var authRepository: AuthRepository
    private  var userData : MutableLiveData<FirebaseUser>
    private  var userLoggedStatus : MutableLiveData<Boolean>
    //private lateinit var userName : String

    fun getFirebaseUserMutableLiveData() : MutableLiveData<FirebaseUser>{
        return userData
    }

    fun getUserLoggedStatus() : MutableLiveData<Boolean>{
        return userLoggedStatus
    }

   /* fun getUserName() : String{
        return userName
    }*/

    init {
        authRepository = AuthRepository(application)
        userData = authRepository.getFirebaseUserMutableLiveData()
        userLoggedStatus = authRepository.getUserLoggedStatus()
       // userName = authRepository.getUserName()
    }

    fun register(email : String, password: String,name: String){
        authRepository.register(email,password,name)
    }

    fun login(userEmail : String, userPassword: String){
        authRepository.login(userEmail,userPassword)
    }

    fun logout(){
        authRepository.signout()
    }
}