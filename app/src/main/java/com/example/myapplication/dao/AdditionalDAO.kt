package com.example.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.myapplication.entity.Additional
import com.example.myapplication.entity.Passport

@Dao
interface AdditionalDAO : BaseDAO<Additional> {

    @Query("select * from additional_table")
    fun  getAll(): LiveData<List<Additional>>

    @Query("delete from additional_table")
    fun deleteAll()

    @Query("select * from additional_table where add_id = :add_id")
    fun getById(add_id: Long): Additional
}