package com.najma.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.najma.githubuser.data.response.ListUser
import com.najma.githubuser.data.response.SearchResponse
import com.najma.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUser = MutableLiveData<List<ListUser>>()
    val listUser: LiveData<List<ListUser>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isDataError = MutableLiveData<Boolean>()
    val isDataError: LiveData<Boolean> = _isDataError

    companion object{
        private const val TAG = "MainViewModel"
    }

    init{
        findGithub()
    }

    fun findGithub() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser()
        client.enqueue(object : Callback<List<ListUser>> {
            override fun onResponse(
                call: Call<List<ListUser>>,
                response: Response<List<ListUser>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _isDataError.value = false
                    _listUser.value = response.body()
                } else {
                    _isDataError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ListUser>>, t: Throwable) {
                _isDataError.value = true
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun searchUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUser(username)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    _listUser.value = response.body()?.items
                    _isDataError.value = _listUser.value.isNullOrEmpty()
                } else {
                    _isDataError.value = true
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _isDataError.value = true
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })
    }

}