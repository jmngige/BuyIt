package com.starsolns.e_shop.util

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.starsolns.e_shop.R

class ProgressButton(ctx: Context, view: View) {
    val button = view.findViewById<ConstraintLayout>(R.id.login_register_access_button)
    val progressBar = view.findViewById<ProgressBar>(R.id.login_register_access_progress_bar)
    val btn_text = view.findViewById<TextView>(R.id.login_register_access_text)

    fun showLogin(){
        btn_text.text = "Login"
    }

    fun showRegister(){
        btn_text.text = "Register"
    }

    fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
        btn_text.text = "Please wait..."
    }

    fun dismissProgressBar(){
        progressBar.visibility = View.INVISIBLE
        btn_text.text = "Successful"
    }
}