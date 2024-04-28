package com.dicoding.worldcountries.data.network.remote

import com.google.gson.annotations.SerializedName

data class CityResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("data")
	val data: List<String>,

	@field:SerializedName("error")
	val error: Boolean
)
