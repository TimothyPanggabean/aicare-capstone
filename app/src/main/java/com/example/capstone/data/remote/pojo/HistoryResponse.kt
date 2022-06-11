package com.example.capstone.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("listHistory")
	val listHistory: List<ListHistoryItem?>? = null
)

data class ListHistoryItem(

	@field:SerializedName("date")
	val date: Date? = null,

	@field:SerializedName("outputpercentage")
	val outputpercentage: String? = null,

	@field:SerializedName("outputlabel")
	val outputlabel: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userid")
	val userid: String? = null
)

data class Date(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val seconds: Int? = null
)
