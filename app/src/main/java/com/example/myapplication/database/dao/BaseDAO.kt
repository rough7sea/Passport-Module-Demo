package com.example.myapplication.database.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

interface BaseDAO<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(objs: List<T>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: T): Long

    @Update
    fun update(obj: T): Int

    @Delete
    fun delete(obj: T): Int

    @RawQuery
    fun getWithParameters(query: SupportSQLiteQuery): List<T>

    @RawQuery
    fun getCurrentObjectWithParameters(query: SupportSQLiteQuery): T

}