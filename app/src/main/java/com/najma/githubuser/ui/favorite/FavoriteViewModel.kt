package com.najma.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.najma.githubuser.data.database.UserRepo
import com.najma.githubuser.data.database.UserEntity

class FavoriteViewModel(application: Application): ViewModel() {

    private val mUserRepo: UserRepo = UserRepo(application)

    fun getFavUser(): LiveData<List<UserEntity>> = mUserRepo.getListFavUser()
}