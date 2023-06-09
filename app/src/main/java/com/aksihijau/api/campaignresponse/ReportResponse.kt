package com.aksihijau.api.campaignresponse

import com.google.gson.annotations.SerializedName

data class ReportResponse(
    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("data")
    val data: List<Report>? = null,

    @SerializedName("errors")
    val errors: Any? = null
)

data class Report(
    @SerializedName("id")
    val id: Int? = null,


    @SerializedName("cratorName")
    val cratorName: String? = null,

    @SerializedName("cratorImage")
    val cratorImage: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("body")
    val body: String? = null
)