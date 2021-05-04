package com.example.datamanager.database.repository.impl

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.datamanager.database.dao.AdditionalDAO
import com.example.datamanager.database.entity.Additional
import com.example.datamanager.database.repository.Repository
import com.example.datamanager.utli.QueryBuilder

class AdditionalRepository(private val additionalDAO: AdditionalDAO) : Repository<Additional>{

    fun findAllByTowerId(tower_id: Long) = additionalDAO.getByTowerId(tower_id)

    fun findAllByCoordinateId(coord_id: Long) = additionalDAO.getByCoordinateId(coord_id)

    override fun getData(): LiveData<List<Additional>> = additionalDAO.getAll()

    override fun getById(id: Long): Additional? = additionalDAO.getById(id)

    override fun add(addObject: Additional): Long = additionalDAO.insert(addObject)

    override fun addAll(addObjects: List<Additional>) = additionalDAO.insert(addObjects)

    override fun update(updateObject: Additional) = additionalDAO.update(updateObject)

    override fun delete(deleteObject: Additional)  = additionalDAO.delete(deleteObject)

    override fun deleteAll() = additionalDAO.deleteAll()

    override fun findWithParameters(query: SupportSQLiteQuery): List<Additional> =
        additionalDAO.getWithParameters(query)

    override fun findCurrentWithParameter(query: SupportSQLiteQuery): Additional? =
        additionalDAO.getCurrentObjectWithParameters(query)

    override fun findAll(): List<Additional> =
        additionalDAO.getWithParameters(QueryBuilder.buildSelectAllQuery(Additional::class.java))

}