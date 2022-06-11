package com.example.capstone.ui.viewmodel

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.capstone.data.local.UserSession
import com.example.capstone.data.remote.api.ApiConfig
import com.example.capstone.data.remote.pojo.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestModelViewModel(private val pref: UserSession) : ViewModel() {

    var userToken : LiveData<String> = pref.getToken().asLiveData()

    fun uploadResult(token: String, label: String, percentage: Float, context: Context) {
        val client = ApiConfig.getApiService().uploadResult("Bearer " + token, label, percentage)
        client.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    if (responseBody.error == false) {
                        println("harusnya success")
                        Toast.makeText(context, responseBody.message, Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        responseBody?.message ?: "Upload Failed",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }

        })
    }
}