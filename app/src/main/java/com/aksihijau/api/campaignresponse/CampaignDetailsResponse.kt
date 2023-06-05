package com.aksihijau.api.campaignresponse

import com.google.gson.annotations.SerializedName

data class CampaignDetailsResponse(
    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("data")
    val data: CampaignDetailsData? = null,

    @SerializedName("errors")
    val errors: Any? = null
)

data class CampaignDetailsData(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("soilId")
    val soilId: Int? = null,

    @SerializedName("slug")
    val slug: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("target")
    val target: Int? = null,

    @SerializedName("deadline")
    val deadline: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("reportsCount")
    val reportsCount: Int? = null,

    @SerializedName("fundraiser")
    val fundraiser: Fundraiser? = null,

    @SerializedName("soil")
    val soil: Soil? = null,

    @SerializedName("donationsCount")
    val donationsCount: Int? = null,

    @SerializedName("remainingDays")
    val remainingDays: Int? = null,

    @SerializedName("collected")
    val collected: Int? = null,

    @SerializedName("active")
    val active: Boolean? = null,

    @SerializedName("latestDonations")
    val latestDonations: List<Donation>? = null,

)

data class Fundraiser(
    @SerializedName("photo")
    val photo: String? = null,

    @SerializedName("name")
    val name: String? = null
)
