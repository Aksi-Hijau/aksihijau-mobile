package com.aksihijau.api

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("data")
	val data: DataUser? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("errors")
	val errors: Any? = null
)

data class DataUser(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("session")
	val session: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("photo")
	val photo: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("exp")
	val exp: Int? = null,

	@field:SerializedName("uuid")
	val uuid: String? = null,

	@field:SerializedName("birthDate")
	val birthDate: Any? = null,

	@field:SerializedName("iat")
	val iat: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
