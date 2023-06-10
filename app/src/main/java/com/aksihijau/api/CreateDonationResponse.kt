package com.aksihijau.api

import com.google.gson.annotations.SerializedName

data class CreateDonationResponse(

	@field:SerializedName("data")
	val data: DataCreateDonation? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("errors")
	val errors: Errors? = null
)

data class Ewallet(

	@field:SerializedName("instruction")
	val instruction: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("actions")
	val actions: List<ActionsItem?>? = null
)

data class DataCreateDonation(

	@field:SerializedName("donorName")
	val donorName: String? = null,

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("payment")
	val payment: PaymentMethod? = null,

	@field:SerializedName("invoice")
	val invoice: String? = null,

	@field:SerializedName("deadline")
	val deadline: String? = null
)

data class PaymentMethod(

	@field:SerializedName("ewallet")
	val ewallet: Ewallet? = null,

	@field:SerializedName("bank")
	val bank: Bank? = null,

	@field:SerializedName("_links")
	val links: LinksCreateDonation? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("logo")
	val logo: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class ActionsItem(

	@field:SerializedName("method")
	val method: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)


data class Bank(

	@field:SerializedName("vaNumber")
	val vaNumber: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class LinksCreateDonation(

	@field:SerializedName("instruction")
	val instruction: String? = null
)
