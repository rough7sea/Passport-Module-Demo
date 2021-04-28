package com.example.myapplication

import android.app.Application
import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.room.Room
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.external.handler.ObjectBindingHandler
import com.example.myapplication.external.handler.impl.ObjectBindingHandlerImpl

class App : Application() {

    companion object{
        @Volatile
        private lateinit var appDatabase: AppDatabase

        private lateinit var context: Context

        private lateinit var dataManager: IPassportManager<Any>

        fun getAppContext() = context

        fun getDatabase() = appDatabase

        fun getDataManager() = dataManager
    }



    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        synchronized(this){
            appDatabase = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "passportModuleDB"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
        dataManager = DataManager(appDatabase, getSystemService(LOCATION_SERVICE) as LocationManager)
        Log.d("DEBUG", "OnCreate App")
    }
}