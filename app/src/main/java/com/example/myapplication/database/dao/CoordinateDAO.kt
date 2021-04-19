package com.example.myapplication.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.myapplication.database.DatabaseConst
import com.example.myapplication.database.entity.Coordinate

@Dao
interface CoordinateDAO : BaseDAO<Coordinate> {

    @Query("select * from ${DatabaseConst.COORDINATE_TABLE_NAME}")
    fun  getAll(): LiveData<List<Coordinate>>

    @Query("delete from ${DatabaseConst.COORDINATE_TABLE_NAME}")
    fun deleteAll()

    @Query("select * from ${DatabaseConst.COORDINATE_TABLE_NAME} where coord_id = :coord_id")
    fun getById(coord_id: Long): Coordinate

    @Query("select * from ${DatabaseConst.COORDINATE_TABLE_NAME} where longitude = :longitude and latitude = :latitude")
    fun getByLongitudeAndLatitude(longitude: Double, latitude: Double): Coordinate?

    @RawQuery
    fun getCoordsInRadius(query: SupportSQLiteQuery) : List<Coordinate>
}