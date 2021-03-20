package com.example.myapplication

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.myapplication.database.AppDatabase

class App : Application() {

    companion object{
        @Volatile
        private lateinit var dataManager: AppDatabase

        fun getDatabaseManager(): AppDatabase {
            return dataManager
        }
    }

    override fun onCreate() {
        super.onCreate()
        synchronized(this){
            dataManager = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "test_DB")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
        }
        Log.d("DEBUG", "OnCreate App")
    }

}