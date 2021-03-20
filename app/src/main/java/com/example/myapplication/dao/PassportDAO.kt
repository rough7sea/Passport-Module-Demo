package com.example.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.myapplication.dao.BaseDAO
import com.example.myapplication.entity.Coordinate
import com.example.myapplication.entity.Passport

@Dao
interface PassportDAO : BaseDAO<Passport> {

    @Query("select * from passport_table")
    fun  getAll(): LiveData<List<Passport>>

    @Query("delete from passport_table")
    fun deleteAll()

//    @Query("select * from passport_table where passport_id = :passport_id")
//    fun getById(passport_id: Long): Passport
}