package com.example.ecommercesalesapp.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.ecommercesalesapp.R
import com.example.ecommercesalesapp.viewModel.LoginRegisterViewModel
import com.google.firebase.auth.FirebaseUser


class LoginRegistrationFragment : Fragment() {

    lateinit var nameTxtView: TextView
    lateinit var emailTxt: TextView
    lateinit var passwordTxtView: TextView
    lateinit var confirmPwdTxtView: TextView
    lateinit var signUpBtn: Button
    lateinit var footerTxt: TextView
    lateinit var loginTxt: TextView
    lateinit var loginRegisterViewModel: LoginRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Assign value to viewModel, set observer for user change
        loginRegisterViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)

        loginRegisterViewModel.getFirebaseUserMutableLiveData().observe(this, Observer<FirebaseUser>{
            //Check if FirebaseUser not null
            if(it != null){
                displayHomeActivity()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Get value from Splash Activity and store in variable in Fragment
        val myValue = this.getArguments()?.getString("profiletype")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login_registration, container, false)

        nameTxtView = view.findViewById(R.id.nameTextView)
        emailTxt = view.findViewById(R.id.emailTextView)
        passwordTxtView = view.findViewById(R.id.passwordTextView)
        confirmPwdTxtView = view.findViewById(R.id.confirmPwdTextView)
        signUpBtn = view.findViewById(R.id.signUpBtn)
        footerTxt = view.findViewById(R.id.registrationFooterTxt)
        loginTxt = view.findViewById(R.id.registrationFooterLoginTxt)

        loginTxt.setOnClickListener {
            displayFieldsInFragByValue(loginTxt.text.toString())
        }

        signUpBtn.setOnClickListener {
            implementFunctionalityByValue(signUpBtn.text.toString())
        }

        if (myValue != null) {
            displayFragemntByValue(myValue)
        }

        return view
    }

    fun displayFragemntByValue(profileValue: String) {
        if (profileValue == "SignUp") {
            nameTxtView.isVisible = true
            emailTxt.isVisible = true
            passwordTxtView.isVisible = true
            confirmPwdTxtView.isVisible = true
            signUpBtn.text = getString(R.string.signUpBtnTxt)
            footerTxt.text = getString(R.string.registrationFooterTxt)
            loginTxt.text = getString(R.string.registratonFooterLoginTxt)
        }

        if (profileValue == "Login") {
            nameTxtView.isVisible = false
            emailTxt.isVisible = true
            passwordTxtView.isVisible = true
            confirmPwdTxtView.isVisible = false
            signUpBtn.text = getString(R.string.loginBtnTxt)
            footerTxt.text = getString(R.string.registrationFooterNoAct)
            loginTxt.text = getString(R.string.registrationFooterSignupTxt)
        }
    }

    fun displayFieldsInFragByValue(footerTxtValue: String)
    {
        if(footerTxtValue.equals("Signup", ignoreCase = true))
        {
            nameTxtView.isVisible = true
            emailTxt.isVisible = true
            passwordTxtView.isVisible = true
            confirmPwdTxtView.isVisible = true
            signUpBtn.text = getString(R.string.signUpBtnTxt)
            footerTxt.text = getString(R.string.registrationFooterTxt)
            loginTxt.text = getString(R.string.registratonFooterLoginTxt)
            clearFields()
            //Refresh & reload fragment on textview click
            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
        }

        if(footerTxtValue.equals("Login", ignoreCase = true))
        {
            nameTxtView.isVisible = false
            emailTxt.isVisible = true
            passwordTxtView.isVisible = true
            confirmPwdTxtView.isVisible = false
            signUpBtn.text = getString(R.string.loginBtnTxt)
            footerTxt.text = getString(R.string.registrationFooterNoAct)
            loginTxt.text = getString(R.string.registrationFooterSignupTxt)
            clearFields()
            //Refresh & reload fragment on textview click
            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
        }
    }

    fun clearFields()
    {
        nameTxtView.setText("")
        emailTxt.setText("")
        passwordTxtView.setText("")
        confirmPwdTxtView.setText("")
    }

    fun implementFunctionalityByValue(btnTextValue: String){
        val registeredEmail = emailTxt.text.toString()
        val registeredPassword = passwordTxtView.text.toString()
        val registeredUsername = nameTxtView.text.toString()

        if(btnTextValue.equals("Signup", ignoreCase = true)) {
            if ((nameTxtView.text.toString().isEmpty()) || (emailTxt.text.toString()
                    .isEmpty() || (passwordTxtView.text.toString().isEmpty()))
            ) {
                Toast.makeText(context, "Please Enter Name, Email  & Password", Toast.LENGTH_LONG)
                    .show()
            } else if (validateEmail(emailTxt.text.toString()) && validatePwd(passwordTxtView.text.toString())) {
                    //Register user using FirebaseAuth
                    loginRegisterViewModel.register(registeredEmail,registeredPassword,registeredUsername)
            }
        }

        else if(btnTextValue.equals("Login", ignoreCase = true))
        {
            if(validateEmail(emailTxt.text.toString()))
            {
                loginRegisterViewModel.login(emailTxt.text.toString(),passwordTxtView.text.toString())
            }
            else{
                Toast.makeText(context, "Please Enter Valid Email & Password", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun validateEmail(email: String):Boolean{
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            return true
        }
        else
        {
            emailTxt.setError("Invalid Email Format")
            return false
        }
    }

    fun validatePwd(pwd: String):Boolean{
        //Check if password is lessthan 6 characters
        if(pwd.length<6)
        {
            passwordTxtView.setError("Password should be greater than or equal to 6 characters")
            return false
        }else if (!(pwd.equals(confirmPwdTxtView.text.toString())))
        {
            confirmPwdTxtView.setError("Password did not match")
            return false
        }
        else
        {
            return true
        }
    }

    fun displayHomeActivity()
    {
        //Call new Activity from fragment
        val intent = Intent(activity,HomeActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }
}