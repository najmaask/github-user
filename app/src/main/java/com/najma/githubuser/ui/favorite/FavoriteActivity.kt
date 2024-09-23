package com.najma.githubuser.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.najma.githubuser.databinding.ActivityFavoriteBinding
import com.najma.githubuser.ui.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val factory = ViewModelFactory.getInstance(this@FavoriteActivity.application)
        val favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        binding?.rvFavorite?.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        binding?.rvFavorite?.layoutManager = layoutManager

        favoriteViewModel.getFavUser().observe(this){ listFavorite ->
            binding?.rvFavorite?.adapter = FavoriteAdapter(listFavorite)
        }

        supportActionBar?.title = "List Favorite"
    }
}