package com.example.barze

import android.net.Uri.encode
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.barze.data.model.Bar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class UpdateBarActivity : AppCompatActivity() {
    private lateinit var submitButton: Button
    val db = FirebaseFirestore.getInstance()
    private lateinit var atmosphere: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_bar)

        val bundle = intent?.getBundleExtra("BUNDLE")
        val bar = bundle?.getParcelable<Bar>("BAR")

        val headerText = findViewById<TextView>(R.id.update_title)
        headerText.text = String.format(getString(R.string.update_header), bar!!.name)

        val wait = findViewById<EditText>(R.id.update_wait)
        wait.setText(bar.wait.toString())

        val cover = findViewById<EditText>(R.id.update_cover)
        cover.setText(bar.cover.toString())

        val deals = findViewById<EditText>(R.id.update_deals)
        deals.setText(bar.deals)

        val events = findViewById<EditText>(R.id.update_events)
        events.setText(bar.events)

        submitButton = findViewById(R.id.update_submit)


        submitButton.setOnClickListener {
            updateBar(wait.text.toString(), cover.text.toString(), deals.text.toString(), events.text.toString(), bar)
            finish()
        }
    }

    private fun updateBar(waitText: String, coverText: String, dealsText: String, eventsText: String, bar: Bar) {
        val id = encode(bar.name)
        val atmosphere = findViewById<RadioGroup>(R.id.update_radioGroup)
        val checkedId = atmosphere.checkedRadioButtonId
        val checkedButton = findViewById<RadioButton>(checkedId)

        val data = hashMapOf(
            "wait" to waitText.toInt(),
            "cover" to coverText.toFloat(),
            "atmosphere" to checkedButton.text,
            "deals" to dealsText,
            "events" to eventsText
        )

        db.collection("bars").document(id).set(data, SetOptions.merge())
    }
}
