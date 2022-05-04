package com.starsolns.e_shop.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.ActivityHomeBinding
import com.starsolns.e_shop.model.Users
import com.starsolns.e_shop.util.Constants

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!.uid

        val db = FirebaseFirestore.getInstance()
        db.collection(Constants.USERS)
            .document(currentUser)
            .get()
            .addOnSuccessListener { result->
               val user = result.toObject<Users>()
                binding.namesy.text = user!!.firstName
                Log.i("TAG", user!!.firstName)
            }


    }
}