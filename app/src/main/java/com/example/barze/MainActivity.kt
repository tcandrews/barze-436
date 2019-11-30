package com.example.barze

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.barze.data.model.Bar
import com.example.barze.ui.login.LoginActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_bars.*

class MainActivity : AppCompatActivity(), BarsFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Start login activity if not logged in
        val sharedPref = getSharedPreferences("BARZE_SETTINGS", Context.MODE_PRIVATE)
        if (!sharedPref.getBoolean("LOGGED_IN", false)) {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val reviewIntent = Intent(this, ReviewActivity::class.java)
            startActivity(reviewIntent)
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.action_sign_out -> {
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }
            R.id.list_view-> {
                true
            }
            R.id.map_view-> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onListFragmentInteraction(item: Bar?) {
        Snackbar.make(findViewById(R.id.fragment), item!!.name, Snackbar.LENGTH_SHORT)
            .setAction("Action", null).show()
        // TODO pop up launched - hours, happy hour times, link to menu, deals
    }
}
