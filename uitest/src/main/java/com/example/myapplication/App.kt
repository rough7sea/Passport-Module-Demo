package com.example.myapplication

import android.app.Application
import android.location.LocationManager
import android.util.Log
import com.example.datamanager.DataManager

class App : Application() {

    companion object{
        private lateinit var dataManager: DataManager
        fun getDataManager() = dataManager
    }

    override fun onCreate() {
        super.onCreate()
        val locationManager =
            applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        dataManager = DataManager.initialize(applicationContext, locationManager).build()
        Log.d("DEBUG", "OnCreate App")
    }
}