package com.example.myapplication

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.myapplication.database.AppDatabase

class App : Application() {

    companion object{
        @Volatile
        private lateinit var dataManager: AppDatabase

        private lateinit var context: Context

        fun getAppContext() = context

        fun getDatabaseManager()= dataManager
    }



    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        synchronized(this){
            dataManager = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "test_DB"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
        Log.d("DEBUG", "OnCreate App")
    }
}