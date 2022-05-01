package com.starsolns.e_shop.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ProgressBar
import com.github.ybq.android.spinkit.style.Circle
import com.github.ybq.android.spinkit.style.Wave
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        progressBar = binding.progressView
        val circle = Circle()
        progressBar.indeterminateDrawable = circle

        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(0,0)
            },4000)
        }


    }
}