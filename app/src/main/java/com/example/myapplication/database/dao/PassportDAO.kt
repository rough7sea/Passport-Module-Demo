package com.example.myapplication.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.myapplication.database.DatabaseConst
import com.example.myapplication.database.entity.Passport

@Dao
interface PassportDAO : BaseDAO<Passport> {

    @Query("select * from ${DatabaseConst.PASSPORT_TABLE_NAME}")
    fun getAll(): LiveData<List<Passport>>

    @Query("delete from ${DatabaseConst.PASSPORT_TABLE_NAME}")
    fun deleteAll()

    @Query("select * from ${DatabaseConst.PASSPORT_TABLE_NAME} where passport_id = :passport_id")
    fun getById(passport_id: Long): Passport
}