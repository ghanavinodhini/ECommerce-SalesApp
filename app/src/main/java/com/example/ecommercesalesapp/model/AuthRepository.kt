package com.example.ecommercesalesapp.model

import android.app.Activity
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository {
    private var app : Application
    private var firebaseUserMutableLiveData : MutableLiveData<FirebaseUser>
    private var userLoggedStatus : MutableLiveData<Boolean>
    private var auth : FirebaseAuth
    private lateinit var currentUser : FirebaseUser


    constructor(application:Application){
        app = application
        firebaseUserMutableLiveData = MutableLiveData()
        userLoggedStatus = MutableLiveData(false)
        auth = FirebaseAuth.getInstance()

        //Check if currentuser available
        if(auth.currentUser != null){
            currentUser = auth.currentUser!!
            firebaseUserMutableLiveData.postValue(currentUser)
        }
    }

    fun getFirebaseUserMutableLiveData() : MutableLiveData<FirebaseUser>{
        return firebaseUserMutableLiveData
    }

    fun getUserLoggedStatus() : MutableLiveData<Boolean>{
        return userLoggedStatus
    }

    fun register(email:String,password:String,name:String){
        Log.d("!!!", "Inside register account")
        auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("!!!", "User account created")
                currentUser = auth.getCurrentUser()!!
                Log.d("!!!", "current user: $currentUser")
                firebaseUserMutableLiveData.postValue(currentUser)
                Toast.makeText(app, "SignUp successfull", Toast.LENGTH_SHORT).show()


               /* val newUserDetails = hashMapOf("Name" to name, "Email" to email)
                db.collection("users").document(auth.currentUser!!.uid)
                    .set(newUserDetails)
                    .addOnSuccessListener { documentReference ->
                        Log.d("!!!", "Document addded")
                    }*/
            } else {
                Toast.makeText(app, "Registration failed: " + task.exception, Toast.LENGTH_LONG)
                    .show()
                Log.d("!!!", "Task New User add Not Success: ${task.exception}")

            }
        }
    }

    fun login(userEmail:String,userPassword: String){
        Log.d("!!!","Inside Login function")
        auth.signInWithEmailAndPassword(userEmail,userPassword)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d("!!!", "Login Success")
                    currentUser = auth.getCurrentUser()!!
                    Log.d("!!!", "current user: $currentUser")
                    firebaseUserMutableLiveData.postValue(currentUser)
                    Toast.makeText(app, "Login Successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(app, "Login Not Success: " + task.exception, Toast.LENGTH_LONG).show()
                    Log.d("!!!", "Login Not Success: ${task.exception}")

                }
            }
    }

    fun signout(){
        Log.d("!!!","Inside Signout function")
        auth.signOut()
        userLoggedStatus.postValue(true)
    }

}