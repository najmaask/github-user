package com.najma.githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.najma.githubuser.R
import com.najma.githubuser.data.response.ListUser
import com.najma.githubuser.databinding.ActivityMainBinding
import com.najma.githubuser.ui.UserAdapter
import com.najma.githubuser.ui.favorite.FavoriteActivity
import com.najma.githubuser.utils.SettingPreferences
import com.najma.githubuser.utils.SettingViewModel
import com.najma.githubuser.utils.SettingViewModelFactory
import com.najma.githubuser.utils.dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        mainViewModel.listUser.observe(this) { users ->
            setUserData(users)
        }

        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }

        mainViewModel.isDataError.observe(this) {
            showErrorMessage(it)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean{
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val switchTheme = menu.findItem(R.id.darkmode)

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[
            SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
                switchTheme.setIcon(R.drawable.ic_lightmode)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
                switchTheme.setIcon(R.drawable.ic_darkmode)
            }
        }

        switchTheme.setOnMenuItemClickListener {
            if (!it.isChecked) {
                it.isChecked = true
                settingViewModel.saveThemeSetting(it.isChecked)
            } else {
                it.isChecked = false
                settingViewModel.saveThemeSetting(it.isChecked)
            }
            true
        }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.searchbar_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.searchUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query.isNullOrBlank()) {
                    mainViewModel.findGithub()
                }
                return true
            }
        })
        return true


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                true
            }
            R.id.favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.darkmode -> {
                true
            }
            else -> throw IllegalArgumentException("Tidak ada: " + item.itemId)
        }
    }

    private fun setUserData(listUsers: List<ListUser>) {
        val users = ArrayList<ListUser>()
        for (user in listUsers) {
            val list = ListUser(
                user.login,
                user.avatarUrl
            )
            users.add(list)
        }
        val adapter = UserAdapter(users)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(isDataError: Boolean) {
        if (isDataError) {
            Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT).show()
        }
    }
}