package com.example.husnistoryapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.example.husnistoryapp.databinding.ActivitySplashBinding
import com.example.husnistoryapp.ui.home.HomeActivity
import com.example.husnistoryapp.ui.home.HomeViewModel
import com.example.husnistoryapp.utils.ViewModelFactory

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val homeViewModel by viewModels<HomeViewModel> { ViewModelFactory.getInstance(this) }
    private val duration = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            homeViewModel.getSession().observe(this@SplashActivity) {
                if (it.isLogin) {
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    startActivity(Intent(this, WelcomeActivity::class.java))
                }
                finish()
            }
        },duration)
    }
}