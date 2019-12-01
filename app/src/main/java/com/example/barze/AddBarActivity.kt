package com.example.barze

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_review.*
import java.util.ArrayList


class AddBarActivity : AppCompatActivity() {
    private lateinit var buttonAddBar: FloatingActionButton
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        buttonAddBar = findViewById<View>(R.id.floatingActionButton) as FloatingActionButton

        buttonAddBar.setOnClickListener {
            addBarReview()
        }
    }

    private fun addBarReview() {
        val barName = findViewById<EditText>(R.id.name).text.toString()
        val barId = findViewById<EditText>(R.id.name).text.toString().trim(' ')
        val hours = findViewById<EditText>(R.id.hours).text.toString()
        val wait = findViewById<EditText>(R.id.wait).text.toString()
        val intWait = findViewById<EditText>(R.id.wait).text.toString().toInt()
        val cover = findViewById<EditText>(R.id.cover).text.toString()
        val flCover = findViewById<EditText>(R.id.cover).text.toString().toFloat()
        val hh = findViewById<EditText>(R.id.hh).text.toString()
        val atmosphere = radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                radio.text
            }).toString()
        val deals = findViewById<EditText>(R.id.deals).text.toString()
        val events = findViewById<EditText>(R.id.events).text.toString()
        val menu = findViewById<EditText>(R.id.menu).text.toString()


        if (!TextUtils.isEmpty(barName) || !TextUtils.isEmpty(hours) ||
            !TextUtils.isEmpty(wait) || !TextUtils.isEmpty(cover) ||
            !TextUtils.isEmpty(hh) || !TextUtils.isEmpty(atmosphere) ||
            !TextUtils.isEmpty(deals) || !TextUtils.isEmpty(events)
        ) {
            val bar = com.example.barze.data.model.Bar(barName, flCover, atmosphere, intWait, deals, events, hh, hours, menu)
            db.collection("bars").document().set(bar)

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)

            Toast.makeText(this, "Bar added", Toast.LENGTH_LONG).show()
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter in field", Toast.LENGTH_LONG).show()
        }
    }

    companion object IDs {
        val milkboyId = "0jEahS8r5I28NzAS6dl6"
    }
}