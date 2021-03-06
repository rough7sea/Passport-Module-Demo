package com.example.datamanager.database.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery

/**
 * Main repository API.
 * Implemented class must provide standard functions.
 */
interface Repository<T> {

    fun getData(): LiveData<List<T>>

    fun getById(id: Long) : T?

    fun add(addObject: T): Long

    fun addAll(addObjects: List<T>)

    fun update(updateObject: T)

    fun delete(deleteObject: T)

    fun deleteAll()

    fun findWithParameters(query: SupportSQLiteQuery): List<T>

    fun findCurrentWithParameter(query: SupportSQLiteQuery): T?

    fun findAll(): List<T>
}