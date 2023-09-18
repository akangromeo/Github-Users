package com.example.githubusers.data.retrofit

import com.example.githubusers.data.response.DetailUserResponse
import com.example.githubusers.data.response.FollowsResponse
import com.example.githubusers.data.response.GithubResponse
import com.example.githubusers.data.response.User
import com.example.githubusers.ui.DetailUserActivity
import retrofit2.http.*
import retrofit2.Call

interface ApiService {


    @GET("search/users")
    fun getSearchUser(@Query("q") query: String): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(@Path("username")username: String): Call<List<FollowsResponse>>

    @GET("users/{username}/following")
    fun getUserFollowing(@Path("username")username: String): Call<List<FollowsResponse>>

}