package com.example.datamanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.datamanager.database.DatabaseConst
import com.example.datamanager.database.entity.Coordinate

@Dao
interface CoordinateDAO : BaseDAO<Coordinate> {

    @Query("select * from ${DatabaseConst.COORDINATE_TABLE_NAME}")
    fun  getAll(): LiveData<List<Coordinate>>

    @Query("delete from ${DatabaseConst.COORDINATE_TABLE_NAME}")
    fun deleteAll()

    @Query("select * from ${DatabaseConst.COORDINATE_TABLE_NAME} where coord_id = :coord_id")
    fun getById(coord_id: Long): Coordinate?

    @Query("select * from ${DatabaseConst.COORDINATE_TABLE_NAME} where longitude = :longitude and latitude = :latitude")
    fun getByLongitudeAndLatitude(latitude: Double, longitude: Double): Coordinate?

}