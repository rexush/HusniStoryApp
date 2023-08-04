package com.example.husnistoryapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.husnistoryapp.R
import com.example.husnistoryapp.databinding.ActivityHomeBinding
import com.example.husnistoryapp.model.LoadingStateAdapter
import com.example.husnistoryapp.ui.WelcomeActivity
import com.example.husnistoryapp.ui.maps.MapsActivity
import com.example.husnistoryapp.ui.story.AddStoryActivity
import com.example.husnistoryapp.ui.story.ListStoryAdapter
import com.example.husnistoryapp.utils.ViewModelFactory

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var factory: ViewModelFactory
    private lateinit var storyAdapter: ListStoryAdapter
    private var token = ""
    private val homeViewModel by viewModels<HomeViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupUser()
        setupAdapter()
        setupViewModel()
    }

    private fun setupAction() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun setupUser() {
        showLoading()
        homeViewModel.getSession().observe(this@HomeActivity) {
            token = it.token
            if (!it.isLogin) {
                moveActivity()
            } else {
                setupData()
            }
        }
        showToast()
    }

    private fun setupData() {
        homeViewModel.getListStories.observe(this@HomeActivity) { pagingData ->
            storyAdapter.submitData(lifecycle, pagingData)
        }
    }

    private fun setupAdapter() {
        storyAdapter = ListStoryAdapter()
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun showLoading() {
        homeViewModel.isLoading.observe(this@HomeActivity) {
            binding.pbHome.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun showToast() {
        homeViewModel.toastText.observe(this@HomeActivity) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(
                    this@HomeActivity, toastText, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun moveActivity() {
        startActivity(Intent(this@HomeActivity, WelcomeActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.btn_maps -> {
                startActivity(Intent(this@HomeActivity, MapsActivity::class.java))
                true
            }
            R.id.btn_logout -> {
                homeViewModel.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}