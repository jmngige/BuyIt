package com.starsolns.e_shop.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration


    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.homeFragmentHost) as NavHostFragment
        navController = navHost.findNavController()


        binding.bottomNavView.setupWithNavController(navController)
    }

    fun hideBottomNavBar(){
        binding.bottomNavView.clearAnimation()
        binding.bottomNavView.animate().translationY(binding.bottomNavView.height.toFloat()).duration = 300
        binding.bottomNavView.visibility = View.GONE
    }

    fun showBottomNavBar(){
        binding.bottomNavView.clearAnimation()
        binding.bottomNavView.animate().translationY(0f).duration = 300
        binding.bottomNavView.visibility = View.VISIBLE
    }

    fun setUpSupportCustomActionBar(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)
    }
}