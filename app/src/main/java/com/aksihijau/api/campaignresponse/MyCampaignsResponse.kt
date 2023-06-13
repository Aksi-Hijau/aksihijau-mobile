package com.aksihijau.api.campaignresponse

import com.google.gson.annotations.SerializedName

data class MyCampaignsResponse(

	@field:SerializedName("data")
	val data: List<DataCampaign?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("errors")
	val errors: Any? = null
)

data class DonorsWithLimit(

	@field:SerializedName("method")
	val method: String? = null,

	@field:SerializedName("href")
	val href: String? = null
)

data class Links(

	@field:SerializedName("self")
	val self: Self? = null,

	@field:SerializedName("donorsWithLimit")
	val donorsWithLimit: DonorsWithLimit? = null
)

data class Self(

	@field:SerializedName("method")
	val method: String? = null,

	@field:SerializedName("href")
	val href: String? = null
)

data class DataMyCampaigns(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("_links")
	val links: Links? = null,

	@field:SerializedName("remainingDays")
	val remainingDays: Int? = null,

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("collected")
	val collected: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("deadline")
	val deadline: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null,

	@field:SerializedName("target")
	val target: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
