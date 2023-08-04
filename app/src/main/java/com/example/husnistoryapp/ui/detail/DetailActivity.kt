package com.example.husnistoryapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.husnistoryapp.R
import com.example.husnistoryapp.databinding.ActivityDetailBinding
import com.example.husnistoryapp.service.ListStoryItem

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.title_detail)
            setDisplayHomeAsUpEnabled(true)
        }

        setupData()
    }

    private fun setupData() {
        val data = intent.getParcelableExtra<ListStoryItem>(EXTRA_DATA) as ListStoryItem
        binding.apply {
            tvName.text = data.name
            tvDescription.text = data.description
            Glide.with(this@DetailActivity)
                .load(data.photo)
                .fitCenter()
                .apply(
                    RequestOptions
                        .placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                ).into(imgPhotos)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}