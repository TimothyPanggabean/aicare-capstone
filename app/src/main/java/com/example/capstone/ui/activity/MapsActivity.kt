package com.example.capstone.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.capstone.R
import com.example.capstone.data.local.UserSession
import com.example.capstone.databinding.ActivityMapsBinding
import com.example.capstone.ui.viewmodel.MapsViewModel
import com.example.capstone.ui.viewmodel.factory.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val pref = UserSession.getInstance(dataStore)
        mapsViewModel = ViewModelProvider(this, ViewModelFactory(pref, this))[MapsViewModel::class.java]
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mapsViewModel.getLocation()
        mapsViewModel.hospitalsData.observe(this) {
            for (hospital in it) {
                if (hospital.lat != null && hospital.long != null) {
                    val position = LatLng(hospital.lat, hospital.long)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(hospital.hospitalName)
//                            .snippet(hospital.description)
                    )
                    println(position)
                }
            }
            val firstLocation = LatLng(it[0].lat as Double, it[0].long as Double)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 15f))
        }

        getMyLocation()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}