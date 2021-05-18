package com.example.datamanager.utli

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

/**
 * Util class.
 */
object Utils{
    private val nonLetterRegex = Regex("[A-Za-zА-Яа-я]")

    /**
     * Method to process filed **number**.
     */
    fun clearNumber(number: String) = nonLetterRegex.replace(number, "")

    fun distanceInMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        // haversine great circle distance approximation, returns meters
        val theta = lon1 - lon2
        var dist = (sin(deg2rad(lat1)) * sin(deg2rad(lat2))
                + (cos(deg2rad(lat1)) * cos(deg2rad(lat2))
                * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 // 60 nautical miles per degree of seperation
        dist *= 1852 // 1852 meters per nautical mile
        return dist // in meters
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    fun distanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = sin(Math.toRadians(lat1)) * sin(Math.toRadians(lat2)) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * cos(Math.toRadians(theta))
        dist = acos(dist)
        dist = Math.toDegrees(dist)
        dist *= 60 * 1.1515
        dist *= 1.609344
        return dist // in km
    }
}