package com.example.datamanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.datamanager.database.DatabaseConst
import com.example.datamanager.database.entity.Tower

@Dao
interface TowerDAO : BaseDAO<Tower> {

    @Query("select * from ${DatabaseConst.TOWER_TABLE_NAME} order by tower_id")
    fun  getAll(): LiveData<List<Tower>>

    @Query("select * from ${DatabaseConst.TOWER_TABLE_NAME} where tower_id = :tower_id")
    fun getById(tower_id: Long): Tower?

    @Query("delete from ${DatabaseConst.TOWER_TABLE_NAME}")
    fun deleteAll()

    @Query("select * from ${DatabaseConst.TOWER_TABLE_NAME} where passport_id = :passport_id")
    fun getByPassportId(passport_id: Long): List<Tower>

    @Query("select * from ${DatabaseConst.TOWER_TABLE_NAME} where coord_id = :coord_id")
    fun getByCoordinateId(coord_id: Long): List<Tower>
}