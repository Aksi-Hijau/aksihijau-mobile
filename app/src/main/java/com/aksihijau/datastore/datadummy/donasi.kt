package com.aksihijau.datastore.datadummy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class donasi(
    val photo : Int,
    val nama : String,
    val collect : String,
    val duration :String,
    val company : String,
    val desc : String,
    val soil : String,
): Parcelable
