package com.aksihijau.api

import com.google.gson.annotations.SerializedName

data class DetailInstruksiResponse(

	@field:SerializedName("data")
	val data: DataDetailInstruksi? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class InstructionsItem(

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("body")
	val body: String? = null
)

data class DataDetailInstruksi(

	@field:SerializedName("instructions")
	val instructions: List<InstructionsItem?>,

	@field:SerializedName("payment")
	val payment: PaymentDetailInstruksi? = null
)

data class PaymentDetailInstruksi(

	@field:SerializedName("vaNumber")
	val vaNumber: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("logo")
	val logo: String? = null
)
