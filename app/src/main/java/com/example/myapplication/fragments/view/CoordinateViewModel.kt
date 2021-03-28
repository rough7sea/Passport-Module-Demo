package com.example.myapplication.fragments.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.App
import com.example.myapplication.database.entity.Coordinate
import com.example.myapplication.database.repository.CoordinateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoordinateViewModel(application: Application) : AndroidViewModel(application){

    val readAllData: LiveData<List<Coordinate>>
    private val repository: CoordinateRepository

    init {
        val coordinateDAO = App.getDatabaseManager().coordinateDao()
        repository = CoordinateRepository(coordinateDAO)
        readAllData = repository.allCoordinatesData

        readAllData.observeForever { it ->
            it.forEach {
                println(it)
            }
        }
    }

    fun addCoordinate(coordinate: Coordinate){
        viewModelScope.launch(Dispatchers.IO){
            repository.addCoordinate(coordinate)
        }
    }

}