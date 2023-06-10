package com.aksihijau.api

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class HistoryDetailResponse(

	@field:SerializedName("data")
	val data: DataHistoryDetail? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("errors")
	val errors: Errors? = null
)

@Parcelize
data class Payment(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("type")
	val type: String? = null
) : Parcelable

@Parcelize
data class DataHistoryDetail(

	@field:SerializedName("deadline")
	val deadline: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("_links")
	val links: Links? = null,

	@field:SerializedName("campaignTitle")
	val campaignTitle: String? = null,

	@field:SerializedName("paidAt")
	val paidAt: String? = null,

	@field:SerializedName("payment")
	val payment: Payment? = null,

	@field:SerializedName("invoice")
	val invoice: String? = null,

	@field:SerializedName("campaignImage")
	val campaignImage: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class Links(

	@field:SerializedName("instruction")
	val instruction: String? = null
) : Parcelable

data class Errors(
	val any: Any? = null
)
