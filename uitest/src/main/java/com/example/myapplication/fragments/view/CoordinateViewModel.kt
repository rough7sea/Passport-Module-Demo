package com.example.myapplication.fragments.view

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.repository.impl.CoordinateRepository
import com.example.myapplication.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoordinateViewModel(application: Application) : AndroidViewModel(application){

    val readAllData: LiveData<List<Coordinate>>
    private val repository: CoordinateRepository by lazy {
        App.getDataManager().getRepository(Coordinate::class.java) as CoordinateRepository
    }

    init {
        readAllData = repository.getData()
        readAllData.observeForever {
            Log.i("TEST", "Receive ${it.size} coordinates")
        }
    }

    fun addCoordinate(coordinate: Coordinate){
        viewModelScope.launch(Dispatchers.IO){
            repository.add(coordinate)
        }
    }

}