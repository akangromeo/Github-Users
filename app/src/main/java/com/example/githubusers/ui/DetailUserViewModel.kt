package com.example.githubusers.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusers.data.response.DetailUserResponse
import com.example.githubusers.data.response.FollowsResponse
import com.example.githubusers.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel: ViewModel() {

    private val _user = MutableLiveData<DetailUserResponse>()
    val user = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _userFollowers = MutableLiveData<List<FollowsResponse>>()
    val userFollowers: LiveData<List<FollowsResponse>> = _userFollowers

    private val _userFollowing = MutableLiveData<List<FollowsResponse>>()
    val userFollowing: LiveData<List<FollowsResponse>> = _userFollowing

    private val _toastbarText = MutableLiveData<String>()
    val toastbarText: LiveData<String> = _toastbarText


    fun getDetailUser(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                        _user.value = responseBody!!
                        Log.d(TAG, responseBody.toString())

                }else{
                    _isLoading.value = true
                    Log.e(TAG, response.message())
                    _toastbarText.value = "Loading"
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "$t.message")
                _toastbarText.value = "User Load Failed"
            }
        })
    }

    fun getUserFollowers(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<FollowsResponse>>{
            override fun onResponse(
                call: Call<List<FollowsResponse>>,
                response: Response<List<FollowsResponse>>
            ) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    _userFollowers.value = response.body()
                    val responseBody = response.body()
                    if(responseBody != null){
                        if (responseBody.isEmpty()) {
                            Log.d("FOLLOWERS", responseBody.toString())
                            _toastbarText.value = " This Account have 0 Followers"
                        }
                    }
                    _toastbarText.value = "User Loaded"
                } else{
                    _isLoading.value = true
                    Log.e(TAG, response.message())
                }
            }

            override fun onFailure(call: Call<List<FollowsResponse>>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "$t.message")
                _toastbarText.value = "Failed Load Followers Data"
            }
        })
    }

    fun getUserFollowing(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<FollowsResponse>>{
            override fun onResponse(
                call: Call<List<FollowsResponse>>,
                response: Response<List<FollowsResponse>>
            ) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    _userFollowing.value = response.body()
                    val responseBody = response.body()
                    if(responseBody != null){
                        if (responseBody.isEmpty()) {
                            Log.d("FOLLOWING", responseBody.toString())
                            _toastbarText.value = " This Account have 0 Following"
                        }
                    }
                } else{
                    _isLoading.value = true
                    Log.e(TAG, response.message())
                }
            }

            override fun onFailure(call: Call<List<FollowsResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "$t.message")
                _toastbarText.value = "Failed Load Following Data"
            }
        })
    }

companion object{
    private const val TAG = "DetailUserViewModel"
}
}