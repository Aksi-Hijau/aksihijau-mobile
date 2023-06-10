package com.aksihijau.api

import com.google.gson.annotations.SerializedName

data class PaymentsResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("errors")
	val errors: Any? = null
)

data class EwalletItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("logo")
	val logo: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

data class Data(

	@field:SerializedName("ewallet")
	val ewallet: List<EwalletItem?>? = null,

	@field:SerializedName("bank")
	val bank: List<BankItem?>? = null
)

data class BankItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("logo")
	val logo: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
