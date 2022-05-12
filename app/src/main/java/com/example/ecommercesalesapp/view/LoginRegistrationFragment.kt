package com.example.ecommercesalesapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
        /*loginRegisterViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory
                                    .getInstance(requireActivity().application)).get(loginRegisterViewModel.javaClass)*/
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
        Log.d("!!!", "Inside OnCreateView function in fragment")

        //Assign value to viewModel, set observer for user change
      /*  loginRegisterViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)).get(loginRegisterViewModel.javaClass)

        loginRegisterViewModel.getFirebaseUserMutableLiveData().observe(viewLifecycleOwner, Observer<FirebaseUser>{
            //Check if FirebaseUser not null
            if(it != null){
                Toast.makeText(context,"User Created",Toast.LENGTH_LONG).show()
            }
        })*/

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

            Log.d("!!!", "Inside loginTxt on clicklistener")
            displayFieldsInFragByValue(loginTxt.text.toString())

        }

        signUpBtn.setOnClickListener {

            Log.d("!!!", "Inside signUp button click listener")
            //connectFirebase(signUpBtn.text.toString())
            implementFunctionalityByValue(signUpBtn.text.toString())
        }

        if (myValue != null) {
            Log.d("!!!", "Inside myValue if condition")
            displayFragemntByValue(myValue)
        }

        return view

    }

    fun displayFragemntByValue(profileValue: String) {
        Log.d("!!!", "Inside displayFragemntByValue function")
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
        Log.d("!!!", "Inside displayFieldsInFragByValue function")
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

        Log.d("!!!", "Inside implementFunctionalityByValue function")
        if(btnTextValue.equals("Signup", ignoreCase = true)) {
            if ((nameTxtView.text.toString().isEmpty()) || (emailTxt.text.toString()
                    .isEmpty() || (passwordTxtView.text.toString().isEmpty()))
            ) {
                Log.d("!!!", "Please enter Name,Email & Password")
                Toast.makeText(context, "Please Enter Name, Email  & Password", Toast.LENGTH_LONG)
                    .show()
            } else if (validateEmail(emailTxt.text.toString()) && validatePwd(passwordTxtView.text.toString())) {
                //createUser(nameTxtView.text.toString(), emailTxt.text.toString(), passwordTxtView.text.toString())

                    //Register user using FirebaseAuth
                    loginRegisterViewModel.register(registeredEmail,registeredPassword,registeredUsername)
            }
        }

        else if(btnTextValue.equals("Login", ignoreCase = true))
        {
            Log.d("!!!","Button Text value: $btnTextValue")
            if(validateEmail(emailTxt.text.toString()))
            {
                Log.d("!!!","Before calling loginUser function")
                loginRegisterViewModel.login(emailTxt.text.toString(),passwordTxtView.text.toString())
            }
            else{
                Log.d("!!!","Valid Email and Password not entered")
                Toast.makeText(context, "Please Enter Valid Email & Password", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun validateEmail(email: String):Boolean{
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Log.d("!!!", "Valid Email Address Format")
            return true

        }
        else
        {
            Log.d("!!!", "Invalid Email Address Format")
            emailTxt.setError("Invalid Email Format")
            return false
        }

    }

    fun validatePwd(pwd: String):Boolean{

        Log.d("!!!","Inside validate pwd function")
        //Check if password is lessthan 6 characters
        if(pwd.length<6)
        {
            Log.d("!!!", "Password character size: ${pwd.length}")
            passwordTxtView.setError("Password should be greater than or equal to 6 characters")
            return false
        }else if (!(pwd.equals(confirmPwdTxtView.text.toString())))
        {
            confirmPwdTxtView.setError("Password did not match")
            return false
        }
        else
        {
            Log.d("!!!", "Valid Password length")
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