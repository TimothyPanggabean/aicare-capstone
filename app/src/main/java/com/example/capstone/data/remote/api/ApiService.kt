package com.example.capstone.data.remote.api

import com.example.capstone.data.remote.pojo.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("number") number: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("api/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("api/hospitals")
    fun getHospitals(): Call<HospitalResponse>

    @GET("api/history")
    fun getHistory(
        @Header("Authorization") token: String
    ): Call<HistoryResponse>

    @FormUrlEncoded
    @POST("api/history")
    fun uploadResult(
        @Header("Authorization") token: String,
        @Field("label") label: String,
        @Field("percentage") percentage: Float
    ): Call<UploadResponse>
}