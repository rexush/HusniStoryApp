package com.example.husnistoryapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.husnistoryapp.databinding.ActivityWelcomeBinding
import com.example.husnistoryapp.ui.auth.LoginActivity
import com.example.husnistoryapp.ui.auth.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()
        setupAction()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivWelcome, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.btnLoginWelcome, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.btnRegisterWelcome, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.tvDesc, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }
    }

    private fun setupAction() {
        binding.apply {
            btnLoginWelcome.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
            }
            btnRegisterWelcome.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, RegisterActivity::class.java))
            }
        }
    }
}