package com.najma.githubuser.data.database

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepo(application: Application) {

    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init{
        val db = UserDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getListFavUser(): LiveData<List<UserEntity>> = mUserDao.getAllFavUser()

    fun getFavUser(user: UserEntity) {
        executorService.execute{
            if (mUserDao.isFavorite(user.login)){
                mUserDao.deleteFav(user)
            } else{
                mUserDao.insertFav(user)
            }
        }
    }
}