package com.example.myapplication

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.external.handler.ObjectBindingHandler
import com.example.myapplication.external.handler.impl.ObjectBindingHandlerImpl

class App : Application() {

    companion object{
        @Volatile
        private lateinit var dataManager: AppDatabase

        private lateinit var context: Context

        private lateinit var objectBindingHandler: ObjectBindingHandler

        fun getAppContext() = context

        fun getDatabaseManager() = dataManager

        fun getObjectBindingHandler() = objectBindingHandler
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
        objectBindingHandler = ObjectBindingHandlerImpl(dataManager)
        Log.d("DEBUG", "OnCreate App")
    }
}