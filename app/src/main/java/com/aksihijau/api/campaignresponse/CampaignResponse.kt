package com.aksihijau.api.campaignresponse

import com.google.gson.annotations.SerializedName

data class CampaignResponse(
    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("data")
    val data: List<DataCampaign>? = null,

    @field:SerializedName("errors")
    val errors: Any? = null
)

data class DataCampaign(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("slug")
    val slug: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("target")
    val target: Int? = null,

    @field:SerializedName("deadline")
    val deadline: String? = null,

    @field:SerializedName("collected")
    val collected: Int? = null,

    @field:SerializedName("remainingDays")
    val remainingDays: Int? = null
)