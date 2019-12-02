package com.example.barze

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.barze.data.model.Bar
import com.google.firebase.firestore.FirebaseFirestore

class DeleteBarActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    private lateinit var yesButton : Button
    private lateinit var noButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_bar)

        val bundle = intent?.getBundleExtra("BUNDLE")
        val bar = bundle?.getParcelable<Bar>("BAR")

        yesButton = findViewById(R.id.delete_yes)
        noButton = findViewById(R.id.delete_no)

        yesButton.setOnClickListener {
            deleteBar(bar!!)
            finish()
        }

        noButton.setOnClickListener {
            val returnIntent = Intent(applicationContext, MainActivity::class.java)
            startActivity(returnIntent)
        }
    }

    private fun deleteBar(bar: Bar) {
        val id = when (bar!!.name) {
            "R.J. Bentley's Filling Station" -> bentleys
            "Cornerstone Grill & Loft" -> cornerstone
            "Looney's Pub" -> looneys
            "MilkBoy ArtHouse" -> milkboy
            "El Rey" -> elrey
            "Pizza Kingdom" -> pizza
            else -> turf
        }
        db.collection("bars").document(id).delete()
    }

    companion object {
        const val bentleys = "cW18KjrrOqTBHQH8UGfX"
        const val cornerstone = "oD0jG3JTcnQ6MRteOhYa"
        const val looneys = "CnNysrPTK1faLI6Z8imP"
        const val milkboy = "0jEahS8r5I28NzAS6dl6"
        const val turf = "i1uIEMLH4NiK89DwDGnb"
        const val elrey = "kZeheKVslY1Wx8uepugG"
        const val pizza = "H6t8m9tlu85dlVBy3CwX"
    }
}