package com.example.barze.data.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bar(
    val name: String = "",
    val cover: Float = 0.0F,
    val atmosphere: String = "",
    val wait: Int = 0,
    val deals: String = "",
    val events: String = "",
    val happyHour: String = "",
    val hours: String = "",
    val menu: String = ""
) : Parcelable

