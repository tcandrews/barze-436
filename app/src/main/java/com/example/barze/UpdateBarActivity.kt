package com.example.barze

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.barze.data.model.Bar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_update_bar.*

class UpdateBarActivity : AppCompatActivity() {
    private lateinit var buttonAddBar: FloatingActionButton
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        buttonAddBar = findViewById<View>(R.id.floatingActionButton) as FloatingActionButton

        buttonAddBar.setOnClickListener {
            updateBar()
        }
    }

    private fun updateBar() {
        val bundle = intent.extras
        val bar : Bar? = bundle?.getParcelable("BAR")

    }

}
