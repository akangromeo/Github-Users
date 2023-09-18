package com.example.githubusers.ui

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusers.data.response.GithubResponse
import com.example.githubusers.data.response.User
import com.example.githubusers.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel(){

        private val _user = MutableLiveData<List<User>>()
        val user: LiveData<List<User>> = _user

        private val _isLoading = MutableLiveData<Boolean>()
        val isLoading: LiveData<Boolean> = _isLoading

        private val _toastbarText = MutableLiveData<String>()
        val toastbarText: MutableLiveData<String> = _toastbarText

    init {
        getUser()
    }

    fun getUser(query: String = USER_DEFAULT){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUser(query)
        client.enqueue(object : Callback<GithubResponse>{
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _user.value = response.body()?.items
                    val responseBody = response.body()
                    if (responseBody != null){
                        if (responseBody.totalCount == 0){
                            _toastbarText.value = "Username Not Found"
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _toastbarText.value = "Input username!"
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _toastbarText.value = "Data Failed"
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val USER_DEFAULT = "akangromeo"
    }
}