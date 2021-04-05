package com.example.myapplication.database.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.database.dao.CoordinateDAO
import com.example.myapplication.database.entity.Coordinate

class CoordinateRepository(private val coordinateDAO: CoordinateDAO) {

    val allCoordinatesData: LiveData<List<Coordinate>> = coordinateDAO.getAll()

    fun addCoordinate(coordinate: Coordinate): Long = coordinateDAO.insert(coordinate)

    fun updateCoordinate(coordinate: Coordinate){
        coordinateDAO.update(coordinate)
    }

    fun deleteCoordinate(coordinate: Coordinate){
        coordinateDAO.delete(coordinate)
    }

    fun deleteAllCoordinate(){
        coordinateDAO.deleteAll()
    }

    fun getCoordinateByLongitudeAndLatitude(longitude: Int, latitude: Int) : Coordinate?{
        return coordinateDAO.getByLongitudeAndLatitude(longitude, latitude)
    }

}