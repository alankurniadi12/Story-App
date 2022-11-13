package com.alankurniadi.storyapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.alankurniadi.storyapp.databinding.ActivityDetailStoryBinding
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private val args: DetailStoryActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = args.data

        with(binding) {
            Glide.with(applicationContext)
                .load(data.photoUrl)
                .circleCrop()
                .into(ivDetailPhoto)

            tvDetailName.text = data.name
            tvDetailDescription.text = data.description
        }

    }
}
