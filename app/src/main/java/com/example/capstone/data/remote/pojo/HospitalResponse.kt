package com.example.capstone.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class HospitalResponse(

	@field:SerializedName("listHospital")
	val listStory: List<ListHospitalItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ListHospitalItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("hospitalName")
	val hospitalName: String? = null,

	@field:SerializedName("lat")
	val lat: Double? = null,

	@field:SerializedName("long")
	val long: Double? = null
)
