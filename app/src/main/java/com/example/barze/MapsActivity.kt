package com.example.barze

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.barze.ui.login.LoginActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.model.*
import javax.annotation.Nullable

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private lateinit var mMap: GoogleMap
    private lateinit var mLocationRequest : LocationRequest
    private lateinit var mLastlocation : Location
    private lateinit var mGoogleAPIClient : GoogleApiClient
    var latitude = 0.0
    var longitude = 0.0
    private lateinit var mCurrentLocationMarker : Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 2)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (CheckGooglePlayServices()) {
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
        else Toast.makeText(this,"Google Play Serivices not available", Toast.LENGTH_SHORT).show()

        val button = findViewById<Button>(R.id.findNearbyBars)
        button.setOnClickListener{
            placeMarkers()
        }
    }

    private fun CheckGooglePlayServices() :Boolean {
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(this)
        if (result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, 0).show()
            }
            return false
        }
        return true
    }

    override fun onLocationChanged(location: Location?) {
        latitude = location!!.latitude
        longitude = location!!.longitude
        mLastlocation = location
        if (mCurrentLocationMarker != null) {
            mCurrentLocationMarker.remove()
        }
        val latlng = LatLng(latitude, longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latlng)
        markerOptions.title("Current Location")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        mCurrentLocationMarker = mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng))
        mMap.animateCamera(CameraUpdateFactory.zoomBy(13.0f))



    }

    //If we were to fully implement this project this method would help connect to Google Places API
    //that would cost money to search for locations.
    /*
    private fun getURL (latitude:Double, longitude:Double, nearbyPlace:String) {
        googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyBLEPBRfw7sMb73Mr88L91Jqh3tuE4mKsE");
    }
    */
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
            R.id.list_view -> {
                finish()
                true
            }
            R.id.map_view-> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onConnected(@Nullable bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.setInterval(100)
        mLocationRequest.setFastestInterval(1000)
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    protected fun buildGoogleAPIClient() {
        mGoogleAPIClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        buildGoogleAPIClient()
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true)
        }
        else {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show()
        }

    }

    private fun placeMarkers() {
        val college_park = LatLng(38.9897, -76.9378)
        val looneys = LatLng(38.990556, -76.934167)
        mMap.addMarker(MarkerOptions().position(looneys).title("Looney's Pub"))

        val milkboy = LatLng(38.981389, -76.938056)
        mMap.addMarker(MarkerOptions().position(milkboy).title("Milkboy ArtHouse"))

        val cornerstone = LatLng(38.980556, -76.938056)
        mMap.addMarker(MarkerOptions().position(cornerstone).title("Cornerstone Grill & Loft"))

        val bents = LatLng(38.980278, -76.937222)
        mMap.addMarker(MarkerOptions().position(bents).title("RJ Bentley's Restaurant"))

        val rail = LatLng(38.979722, -76.9375)
        mMap.addMarker(MarkerOptions().position(rail).title("The Rail Sports Bar"))

        mMap.moveCamera(CameraUpdateFactory.newLatLng(college_park))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f))
    }
}
