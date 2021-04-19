package com.example.myapplication.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.myapplication.database.DatabaseConst
import com.example.myapplication.database.entity.Additional

@Dao
interface AdditionalDAO : BaseDAO<Additional> {

    @Query("select * from ${DatabaseConst.ADDITIONAL_TABLE_NAME}")
    fun  getAll(): LiveData<List<Additional>>

    @Query("delete from ${DatabaseConst.ADDITIONAL_TABLE_NAME}")
    fun deleteAll()

    @Query("select * from ${DatabaseConst.ADDITIONAL_TABLE_NAME} where add_id = :add_id")
    fun getById(add_id: Long): Additional

    @Query("select * from ${DatabaseConst.ADDITIONAL_TABLE_NAME} where tower_id = :tower_id")
    fun getByTowerId(tower_id: Long) : List<Additional>

    @Query("select * from ${DatabaseConst.ADDITIONAL_TABLE_NAME} where coord_id = :coord_id")
    fun getByCoordinateId(coord_id: Long): List<Additional>
}