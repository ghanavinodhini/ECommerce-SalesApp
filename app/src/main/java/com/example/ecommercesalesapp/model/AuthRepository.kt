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
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {
    private var app : Application
    private var firebaseUserMutableLiveData : MutableLiveData<FirebaseUser>
    private var userLoggedStatus : MutableLiveData<Boolean>
    private var auth : FirebaseAuth
     var db: FirebaseFirestore
    private lateinit var currentUser : FirebaseUser


    constructor(application:Application){
        app = application
        firebaseUserMutableLiveData = MutableLiveData()
        userLoggedStatus = MutableLiveData(false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

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

   /* fun getUserName() : String{
        Log.d("!!!", "Inside getUserName AuthRepository")
        var userName:String =""
        val uid = auth.currentUser?.uid
        val collectionRef = db.collection("users")
        Log.d("!!!", "Inside getUserName AuthRepository current user id : " + auth.currentUser?.uid)

            Log.d("!!!", "Inside if uid check AuthRepository")
           collectionRef.document(uid!!).get().addOnCompleteListener {
                    Log.d("!!!", "Inside addOnComplete listener AuthRepository")
                    if(it.isSuccessful){
                        Log.d("!!!", "Inside task successlistener")
                        val document = it.result

                        if(document.exists()){
                            userName = document.getString("Name").toString()
                        }else {
                            Log.d("!!!", "The document doesn't exist.")
                        }
                    }
                    Log.d("!!!", "UserName: $userName")

                }
//                .addOnFailureListener {
//                    Log.d("!!!", "Exception in snapshot: ${it.message}")
//                }
            Log.d("!!!", "Returing user name $userName")
            return userName
    }*/

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

                // Add to users collection in Firestore
                val newUserDetails = hashMapOf("Name" to name, "Email" to email)
                db.collection("users").document(auth.currentUser!!.uid)
                    .set(newUserDetails)
                    .addOnSuccessListener { documentReference ->
                        Log.d("!!!", "Document addded")
                    }
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