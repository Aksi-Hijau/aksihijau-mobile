package com.aksihijau.api.campaignresponse

import com.google.gson.annotations.SerializedName

data class DonaturResponse(
    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("data")
    val data: List<Donation>? = null,

    @SerializedName("errors")
    val errors: Any? = null
)

data class Donation(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("amount")
    val amount: Int? = null,

    @SerializedName("paidAt")
    val paidAt: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("image")
    val image: String? = null
)