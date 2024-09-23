package com.najma.githubuser.data.retrofit
import com.najma.githubuser.data.response.DetailUser
import com.najma.githubuser.data.response.ListUser
import com.najma.githubuser.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService{
    @GET("users")
    fun getUser(): Call<List<ListUser>>

    @GET("search/users")
    fun searchUser(
        @Query("q") login: String
    ) : Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String
    ): Call<DetailUser>

    @GET("users/{login}/followers")
    fun getUserFollower(
        @Path("login") login: String?
    ) : Call<List<ListUser>>

    @GET("users/{login}/following")
    fun getUserFollowing(
        @Path("login") login: String?
    ) : Call<List<ListUser>>
}