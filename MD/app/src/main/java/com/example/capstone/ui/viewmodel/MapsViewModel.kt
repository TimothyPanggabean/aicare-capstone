package com.example.capstone.ui.viewmodel

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.capstone.data.local.UserSession
import com.example.capstone.data.remote.api.ApiConfig
import com.example.capstone.data.remote.pojo.HospitalResponse
import com.example.capstone.data.remote.pojo.ListHospitalItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val pref: UserSession) : ViewModel() {
    private val _hospitalsData = MutableLiveData<List<ListHospitalItem>>()
    val hospitalsData: LiveData<List<ListHospitalItem>> = _hospitalsData

//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading

    fun getLocation() {
        val client = ApiConfig.getApiService().getHospitals()
        client.enqueue(object : Callback<HospitalResponse> {
            override fun onResponse(call: Call<HospitalResponse>, response: Response<HospitalResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    if (responseBody.error == false) {
                        _hospitalsData.value = responseBody.listStory as List<ListHospitalItem>
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<HospitalResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }

        })
    }
}