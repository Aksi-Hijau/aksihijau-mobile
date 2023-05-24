package com.aksihijau.api

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: DataLogin? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("errors")
	val errors: ErrorsLogin? = null
)

data class ErrorsLogin(
	val any: Any? = null
)

data class DataLogin(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("accessToken")
	val accessToken: String,

	@field:SerializedName("refreshToken")
	val refreshToken: String
)
