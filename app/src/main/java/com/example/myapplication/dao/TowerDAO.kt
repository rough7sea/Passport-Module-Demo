package com.example.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.myapplication.entity.Tower

@Dao
interface TowerDAO : BaseDAO<Tower> {
    @Query("select * from tower_table order by tower_id asc")
    fun  getAll(): LiveData<List<Tower>>

    @Query("select * from tower_table where tower_id = :tower_id")
    fun getById(tower_id: Long): Tower

    @Query("delete from tower_table")
    fun deleteAll()

//    @Query("select * from tower_table where passport_id = :passport_id")
//    fun getByPassportId(passport_id: Long): LiveData<List<Tower>>
//
//    @Query("select * from tower_table where coord_id = :coord_id")
//    fun getByCoordinateId(coord_id: Long): Tower
}