package com.example.capstone.data.remote.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UploadResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("newRecord")
	val newRecord: NewRecord? = null
) : Parcelable

@Parcelize
data class NewRecord(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("outputpercentage")
	val outputpercentage: String? = null,

	@field:SerializedName("imageURL")
	val imageURL: String? = null,

	@field:SerializedName("outputlabel")
	val outputlabel: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userid")
	val userid: String? = null
) : Parcelable
