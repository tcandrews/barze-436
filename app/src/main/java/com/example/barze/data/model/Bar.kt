package com.example.barze.data.model
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

object BarContent {
    private const val TAG = "Firestore BarContent"
    val BARS: MutableList<Bar> = ArrayList()
    val BAR_MAP: MutableMap<String, Bar> = HashMap()

    init {
        val db = FirebaseFirestore.getInstance()
        db.collection("bars").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val bar = document.toObject(Bar::class.java)
                    addItem(bar)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

    }

    private fun addItem(bar: Bar) {
        BARS.add(bar)
        BAR_MAP[bar.name] = bar
    }

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
    )
}


