package com.example.barze

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.barze.data.model.Bar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class UpdateBarActivity : AppCompatActivity() {
    private lateinit var submitButton: Button
    private val db = FirebaseFirestore.getInstance()
    private val bundle = intent.getBundleExtra("BUNDLE")
    val bar = bundle!!.getParcelable<Bar>("BAR")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        Log.i("UPDATE BAR", bar.toString())
        val headerText = findViewById<TextView>(R.id.update_title)
        headerText.text = String.format(getString(R.string.update_header), bar!!.name)

        val wait = findViewById<EditText>(R.id.wait)
        wait.setText(bar.wait)

        val cover = findViewById<EditText>(R.id.cover)
        cover.setText(bar.wait)

        val atmosphere = findViewById<RadioGroup>(R.id.radioGroup)
        val checkedId = atmosphere.checkedRadioButtonId
        val checkedButton = findViewById<RadioButton>(checkedId)

        val deals = findViewById<EditText>(R.id.deals)
        deals.setText(bar.deals)

        val events = findViewById<EditText>(R.id.events)
        events.setText(bar.events)

        submitButton = findViewById(R.id.button)

        submitButton.setOnClickListener {
            updateBar(hashMapOf(
                "wait" to wait.text,
                "cover" to cover.text,
                "atmosphere" to checkedButton.text,
                "deals" to deals.text,
                "events" to events.text
            ))
            finish()
        }
    }

    private fun updateBar(data: HashMap<String, Any>) {
        val id = when (bar!!.name) {
            "R.J. Bentley's Filling Station" -> bentleys
            "Cornerstone Grill & Loft" -> cornerstone
            "Looney's Pub" -> looneys
            "MilkBoy ArtHouse" -> milkboy
            else -> turf
        }
        db.collection("bars").document(id).set(data, SetOptions.merge())
    }

    companion object {
        const val bentleys = "cW18KjrrOqTBHQH8UGfX"
        const val cornerstone = "oD0jG3JTcnQ6MRteOhYa"
        const val looneys = "CnNysrPTK1faLI6Z8imP"
        const val milkboy = "0jEahS8r5I28NzAS6dl6"
        const val turf = "i1uIEMLH4NiK89DwDGnb"
    }
}
