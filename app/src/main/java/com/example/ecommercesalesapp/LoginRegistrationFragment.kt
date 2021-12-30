package com.example.ecommercesalesapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction


class LoginRegistrationFragment : Fragment() {

    lateinit var nameTxtView: TextView
    lateinit var emailTxt: TextView
    lateinit var passwordTxtView: TextView
    lateinit var confirmPwdTxtView: TextView
    lateinit var signUpBtn: Button
    lateinit var footerTxt: TextView
    lateinit var loginTxt: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("!!!", "Inside OnCreateView function in fragment")

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
}