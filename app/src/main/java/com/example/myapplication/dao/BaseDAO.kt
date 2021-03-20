package com.example.myapplication.dao

import androidx.room.*

interface BaseDAO<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(objs: List<T>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: T): Long

    @Update
    fun update(obj: T): Int

    @Delete
    fun delete(obj: T): Int

}