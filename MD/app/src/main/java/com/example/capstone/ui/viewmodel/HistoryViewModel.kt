package com.example.capstone.ui.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.capstone.data.local.UserSession
import com.example.capstone.data.remote.api.ApiConfig
import com.example.capstone.data.remote.pojo.HistoryResponse
import com.example.capstone.data.remote.pojo.ListHistoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel(private val pref: UserSession) : ViewModel() {
    var userToken : LiveData<String> = pref.getToken().asLiveData()

    private val _listHistory = MutableLiveData<List<ListHistoryItem>>()
    val listHistory: LiveData<List<ListHistoryItem>> = _listHistory

    fun getUserHistory(token: String) {
        val client = ApiConfig.getApiService().getHistory("Bearer " + token)
        client.enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(
                call: Call<HistoryResponse>,
                response: Response<HistoryResponse>
            ) {
                if (response.isSuccessful) {
                    _listHistory.value = response.body()?.listHistory as List<ListHistoryItem>
                } else
                    Log.e(ContentValues.TAG, "onFailure : ${response.message()}")
            }

            override fun onFailure(
                call: Call<HistoryResponse>,
                t: Throwable
            ) {
                Log.e(ContentValues.TAG, "onFailure : ${t.message}")
            }
        })
    }
}