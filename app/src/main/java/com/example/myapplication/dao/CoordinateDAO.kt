package com.example.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.myapplication.entity.Additional
import com.example.myapplication.entity.Coordinate

@Dao
interface CoordinateDAO : BaseDAO<Coordinate> {

    @Query("select * from coordinate_table")
    fun  getAll(): LiveData<List<Coordinate>>

    @Query("delete from coordinate_table")
    fun deleteAll()

    @Query("select * from coordinate_table where coord_id = :coord_id")
    fun getById(coord_id: Long): Coordinate

//    @Query("select * from coordinate_table where longitude = :longitude and latitude = :latitude")
//    fun getByLongitudeAndLatitude(longitude: Int, latitude: Int,): Coordinate
}