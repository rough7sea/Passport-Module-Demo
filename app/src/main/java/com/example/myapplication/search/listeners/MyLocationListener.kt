package com.example.myapplication.search.listeners

import android.content.ContentValues.TAG
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.util.Log
import android.view.View
import android.widget.Toast
import java.io.IOException
import java.util.*


class MyLocationListener : LocationListener {
    override fun onLocationChanged(loc: Location) {
//        editLocation.setText("")
//        pb.setVisibility(View.INVISIBLE)
//        Toast.makeText(
//            getBaseContext(),
//            "Location changed: Lat: " + loc.latitude.toString() + " Lng: "
//                    + loc.longitude, Toast.LENGTH_SHORT
//        ).show()
        val longitude = "Longitude: " + loc.longitude
        println(longitude)
        Log.v(TAG, longitude)
        val latitude = "Latitude: " + loc.latitude
        Log.v(TAG, latitude)
        println(latitude)

        /*------- To get city name from coordinates -------- */
//        var cityName: String? = null
//        val gcd = Geocoder(, Locale.getDefault())
//        val addresses: List<Address>
//        try {
//            addresses = gcd.getFromLocation(
//                loc.latitude,
//                loc.longitude, 1
//            )
//            if (addresses.isNotEmpty()) {
//                println(addresses[0].locality)
//                cityName = addresses[0].locality
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        val s = """
//            $longitude
//            $latitude
//
//            My Current City is: $cityName
//            """.trimIndent()
    }
}