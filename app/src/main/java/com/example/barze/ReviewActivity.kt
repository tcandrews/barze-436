package com.example.barze

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.activity_review.toolbar
import java.util.ArrayList


class ReviewActivity : AppCompatActivity() {

    internal lateinit var bars: MutableList<Bar>
    internal lateinit var databaseBars: DatabaseReference
    internal lateinit var buttonAddBar: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_review)
        setSupportActionBar(toolbar)

        databaseBars = FirebaseDatabase.getInstance().getReference("bars")
        bars =  ArrayList()

        buttonAddBar = findViewById<View>(R.id.floatingActionButton) as FloatingActionButton

        buttonAddBar.setOnClickListener {
            addBarReview()
        }
    }

    override fun onStart() {
        super.onStart()
        databaseBars.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bars.clear()
                val uid = intent.getStringExtra("USER_ID")
                for (postSnapshot in dataSnapshot.child(uid).children) {
                    val bar = postSnapshot.getValue<Bar>(Bar::class.java)
                    bars.add(bar!!)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun addBarReview() {
        val barName = findViewById<EditText>(R.id.name).text.toString()
        val hours = findViewById<EditText>(R.id.hours).text.toString()
        val wait = findViewById<EditText>(R.id.wait).text.toString()
        val cover = findViewById<EditText>(R.id.cover).text.toString()
        val hh = findViewById<EditText>(R.id.hh).text.toString()
        val atmosphere = radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                radio.text
            }).toString()
        val deals = findViewById<EditText>(R.id.deals).text.toString()
        val events = findViewById<EditText>(R.id.events).text.toString()


        if (!TextUtils.isEmpty(barName) || !TextUtils.isEmpty(hours) ||
            !TextUtils.isEmpty(wait) || !TextUtils.isEmpty(cover) ||
            !TextUtils.isEmpty(hh) || !TextUtils.isEmpty(atmosphere) ||
            !TextUtils.isEmpty(deals) || !TextUtils.isEmpty(events)
        ) {

            val id = databaseBars!!.push().key

            val bar = Bar(id!!, barName, hours, wait, cover, hh, atmosphere, deals, events)

            val uid = intent.getStringExtra("USER_ID")
            databaseBars.child("$uid/$id").setValue(bar)

            findViewById<EditText>(R.id.name).setText(R.string.prompt_name)
            findViewById<EditText>(R.id.hours).setText(R.string.prompt_hours)
            findViewById<EditText>(R.id.wait).setText(R.string.prompt_wait)
            findViewById<EditText>(R.id.cover).setText(R.string.prompt_cover)
            findViewById<EditText>(R.id.hh).setText(R.string.prompt_hh)
            findViewById<EditText>(R.id.deals).setText(R.string.prompt_deals)
            findViewById<EditText>(R.id.events).setText(R.string.prompt_events)


            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)

            Toast.makeText(this, "Bar added", Toast.LENGTH_LONG).show()
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter in field", Toast.LENGTH_LONG).show()
        }
    }

}