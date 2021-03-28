package com.example.myapplication.database.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.database.dao.CoordinateDAO
import com.example.myapplication.database.entity.Coordinate

class CoordinateRepository(private val coordinateDAO: CoordinateDAO) {

    val allCoordinatesData: LiveData<List<Coordinate>> = coordinateDAO.getAll()

    suspend fun addCoordinate(coordinate: Coordinate){
        coordinateDAO.insert(coordinate)
    }

    suspend fun updateCoordinate(coordinate: Coordinate){
        coordinateDAO.update(coordinate)
    }

    suspend fun deleteCoordinate(coordinate: Coordinate){
        coordinateDAO.delete(coordinate)
    }

    suspend fun deleteAllCoordinate(){
        coordinateDAO.deleteAll()
    }

    suspend fun getCoordinateByLongitudeAndLatitude(longitude: Int, latitude: Int) : Coordinate?{
        return coordinateDAO.getByLongitudeAndLatitude(longitude, latitude)
    }

}