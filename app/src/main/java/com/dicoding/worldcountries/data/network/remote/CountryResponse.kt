package com.dicoding.worldcountries.data.network.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class CountryResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("error")
	val error: Boolean
)

@Parcelize
data class DataItem(

	@field:SerializedName("flag")
	val flag: String,

	@field:SerializedName("dialcode")
	val dialcode: String = "",

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("currency")
	val currency: String = "",

	@field:SerializedName("unicodeFlag")
	val unicodeFlag: String = ""
):Parcelable
