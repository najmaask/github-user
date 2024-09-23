package com.najma.githubuser.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.najma.githubuser.R
import com.najma.githubuser.data.response.DetailUser
import com.najma.githubuser.databinding.ActivityDetailBinding
import com.najma.githubuser.ui.follow.SectionsPagerAdapter
import com.najma.githubuser.ui.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val factory = ViewModelFactory.getInstance(this@DetailActivity.application)
        val detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val usernameTag = resources.getString(R.string.username_tag, username)

        detailViewModel.showDetailUser(username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        binding?.viewPager?.adapter = sectionsPagerAdapter
        binding?.viewPager?.offscreenPageLimit = 1

        detailViewModel.detailUser.observe(this) { detailUser ->
            setDetailUser(detailUser)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.isError.observe(this){
            showErrorMessage(it)
        }

        binding?.fabFav?.setOnClickListener {
            detailViewModel.setFavorite()
            Toast.makeText(this, "Berhasil update favorit!", Toast.LENGTH_SHORT).show()
        }

        supportActionBar?.elevation = 0f
        supportActionBar?.title = usernameTag
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun setDetailUser(data: DetailUser) {
        binding?.apply {
            Glide.with(applicationContext)
                .load(data.avatarUrl)
                .circleCrop()
                .into(ivAvaDetail)
            tvNameDetail.text = data.name ?: data.login
            tvUnameDetail.text = data.login
            tvLocDetail.text = data.location ?: "-"
            tvCompanyDetail.text = data.company ?: "-"
            tvFollower.text = data.followers.toString()
            tvFollowing.text = data.following.toString()
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(isError: Boolean){
        if(isError){
            Toast.makeText(this, "Failed to Load Data", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val TAG = "DetailActivity"
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}