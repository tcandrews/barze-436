package com.example.barze.data.model
import java.util.ArrayList

object BarContent {
    val ITEMS: MutableList<Bar> = ArrayList()

    data class Bar(
        val name: String,
        val cover: Float,
        val deals: String,
        val events: String,
        val happyHour: String,
        val menuLink: String,
        val wait: Short
    )
}
