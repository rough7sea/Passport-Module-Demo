package com.example.datamanager.database.repository.impl

import androidx.sqlite.db.SupportSQLiteQuery
import com.example.datamanager.database.dao.BaseDAO
import com.example.datamanager.database.repository.Repository

/**
 * Base implementation of [Repository] functionality.
 */
abstract class BaseRepository<T>(private val dao : BaseDAO<T>) : Repository<T> {

    override fun add(addObject: T): Long = dao.insert(addObject)

    override fun addAll(addObjects: List<T>) = dao.insert(addObjects)

    override fun update(updateObject: T) = dao.update(updateObject)

    override fun delete(deleteObject: T) = dao.delete(deleteObject)

    override fun findWithParameters(query: SupportSQLiteQuery): List<T> =
        dao.getWithParameters(query)

    override fun findCurrentWithParameter(query: SupportSQLiteQuery): T? =
        dao.getCurrentObjectWithParameters(query)

}