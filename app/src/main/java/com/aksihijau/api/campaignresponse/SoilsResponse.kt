package com.aksihijau.api.campaignresponse

import com.google.gson.annotations.SerializedName

data class SoilsResponse(
    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("data")
    val data: Soil? = null,

    @SerializedName("errors")
    val errors: Any? = null
)

data class Soil(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("image")
    val image: String? = null
)
