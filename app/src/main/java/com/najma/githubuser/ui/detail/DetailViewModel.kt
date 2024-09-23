package com.najma.githubuser.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.najma.githubuser.data.database.UserEntity
import com.najma.githubuser.data.database.UserRepo
import com.najma.githubuser.data.response.DetailUser
import com.najma.githubuser.data.response.ListUser
import com.najma.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val _detailUser = MutableLiveData<DetailUser>()
    val detailUser: LiveData<DetailUser> = _detailUser

    private val _listFollow = MutableLiveData<List<ListUser>>()
    val listFollow: LiveData<List<ListUser>> = _listFollow

    private val _favoriteUser = MutableLiveData<UserEntity>()
    private val favoriteUser: LiveData<UserEntity> = _favoriteUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val userRepo: UserRepo = UserRepo(application)

    fun showDetailUser(username: String?) {
        _isLoading.value = true
        val client = username?.let { ApiConfig.getApiService().getDetailUser(it) }
        client?.enqueue(object : Callback<DetailUser> {
            override fun onResponse(
                call: Call<DetailUser>,
                response: Response<DetailUser>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isError.value = false
                    _detailUser.value = response.body()
                    _favoriteUser.value = response.body()?.let { user ->
                        UserEntity(
                            login = user.login,
                            avatarUrl = user.avatarUrl
                        )
                    }
                } else{
                    _isError.value = false
                    Log.e(DetailActivity.TAG, "onFailureResponse : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                _isError.value = false
                _isLoading.value = false
                Log.e(DetailActivity.TAG, "onFailure : ${t.message}")
            }
        })
    }

    fun showListFollower(username: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollower(username)
        client.enqueue(object : Callback<List<ListUser>> {
            override fun onResponse(
                call: Call<List<ListUser>>,
                response: Response<List<ListUser>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollow.value = response.body()
                } else {
                    Log.e(DetailActivity.TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ListUser>>, t: Throwable) {
                _isLoading.value = false
                Log.e(DetailActivity.TAG, "onFailure : ${t.message}")
            }
        })
    }

    fun showListFollowing(username: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<ListUser>> {
            override fun onResponse(
                call: Call<List<ListUser>>,
                response: Response<List<ListUser>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollow.value = response.body()
                } else {
                    Log.e(DetailActivity.TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ListUser>>, t: Throwable) {
                _isLoading.value = false
                Log.e(DetailActivity.TAG, "onFailure : ${t.message}")
            }
        })
    }

    fun setFavorite() = favoriteUser.value?.let { userRepo.getFavUser(it) }
}