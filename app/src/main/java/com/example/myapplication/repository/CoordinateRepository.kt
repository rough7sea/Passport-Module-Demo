package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.dao.CoordinateDAO
import com.example.myapplication.entity.Coordinate

class CoordinateRepository(private val coordinateDAO: CoordinateDAO) {

    val allCoordinatesData: LiveData<List<Coordinate>> = coordinateDAO.getAll()

    suspend fun addCoordinate(coordinate: Coordinate){
        coordinateDAO.insert(coordinate)
    }

}