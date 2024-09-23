package com.najma.githubuser.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllFavUser(): LiveData<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM user WHERE login = :login)")
    fun isFavorite(login: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFav(user: UserEntity)

    @Delete
    fun deleteFav(user: UserEntity)
}