package com.example.lab14

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMap()
    }
    override fun onMapReady(map: GoogleMap) {
        if (hasLocationPermission()) {
            try {
                setupMap(map)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        } else {
            requestLocationPermission()
        }
    }
    private fun loadMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0
        )
    }
    private fun setupMap(map: GoogleMap) {
        try {
            map.isMyLocationEnabled = true
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
        val taipei101 = LatLng(25.033611, 121.565000)
        val taipeiStation = LatLng(25.047924, 121.517081)

        map.addMarker(MarkerOptions().position(taipei101).title("台北101"))
        map.addMarker(MarkerOptions().position(taipeiStation).title("台北車站"))
        map.addPolyline(
            PolylineOptions()
                .add(taipei101, LatLng(25.032435, 121.534905), taipeiStation)
                .color(Color.BLUE)
                .width(10f)
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(25.035, 121.54), 13f))
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 0 && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            loadMap()
        } else {
            finish()
        }
    }
}
