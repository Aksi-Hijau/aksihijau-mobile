package com.aksihijau.ui.payment.pilihnominalmetode

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Payment(
    val id: Int,
    val type: String,
    val method: String,
    val name: String,
    val logo: String,
) : Parcelable
