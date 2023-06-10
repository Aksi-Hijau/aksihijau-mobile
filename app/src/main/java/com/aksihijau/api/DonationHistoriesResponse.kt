package com.aksihijau.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DonationHistoriesResponse(

	@field:SerializedName("data")
	val data: List<DataItemDonationHistories?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("errors")
	val errors: Errors? = null
)

@Parcelize
data class DataItemDonationHistories(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("amount")
	val amount: String,

	@field:SerializedName("_links")
	val links: LinksDonationHistories,

	@field:SerializedName("campaignTitle")
	val campaignTitle: String,

	@field:SerializedName("paidAt")
	val paidAt: String,

	@field:SerializedName("invoice")
	val invoice: String,

	@field:SerializedName("campaignImage")
	val campaignImage: String
) : Parcelable

@Parcelize
data class LinksDonationHistories(

	@field:SerializedName("details")
	val details: String
) : Parcelable

