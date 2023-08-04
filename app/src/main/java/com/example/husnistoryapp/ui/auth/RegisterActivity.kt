package com.example.husnistoryapp.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.husnistoryapp.R
import com.example.husnistoryapp.databinding.ActivityRegisterBinding
import com.example.husnistoryapp.utils.ViewModelFactory

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var factory: ViewModelFactory
    private val registerViewModel: RegisterViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.register)
            setDisplayHomeAsUpEnabled(true)
        }

        playAnimation()
        setupAction()
        setupViewModel()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
        val nameEdit = ObjectAnimator.ofFloat(binding.tlName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailEdit = ObjectAnimator.ofFloat(binding.tlEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val passwordEdit = ObjectAnimator.ofFloat(binding.tlPassword, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, name, nameEdit, email, emailEdit, password, passwordEdit, register)
            startDelay = 500
        }.start()
    }

    private fun setupAction() {
        binding.apply {
            if (editName.length() == 0 && editEmail.length() == 0 && editPassword.length() == 0) {
                editName.error = getString(R.string.required_field_name)
                editEmail.error = getString(R.string.required_field_email)
                editPassword.setError(getString(R.string.required_field_password), null)
                btnRegister.isEnabled = false
            } else if (editName.length() != 0 && editEmail.length() != 0 && editPassword.length() != 0) {
                btnRegister.isEnabled = true
            }

            editEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    btnRegister.isEnabled = editEmail.text!!.isNotEmpty() && editPassword.text!!.isNotEmpty()
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            editPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    btnRegister.isEnabled = editPassword.length() >= 8
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            btnRegister.setOnClickListener {
                showLoading()
                postText()
                showToast()
                moveActivity()
            }
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun showLoading() {
        registerViewModel.isLoading.observe(this@RegisterActivity) {
            binding.pbRegister.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun showToast() {
        registerViewModel.toastText.observe(this@RegisterActivity) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(
                    this@RegisterActivity, toastText, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun moveActivity() {
        registerViewModel.registerResponse.observe(this@RegisterActivity) { response ->
            if (!response.error) {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun postText() {
        binding.apply {
            registerViewModel.doRegister(
                editName.text.toString(),
                editEmail.text.toString(),
                editPassword.text.toString()
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}