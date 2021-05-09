package com.example.datamanager.database.repository.impl

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.datamanager.database.dao.AdditionalDAO
import com.example.datamanager.database.entity.Additional
import com.example.datamanager.database.repository.Repository
import com.example.datamanager.utli.QueryBuilder

class AdditionalRepository(private val additionalDAO: AdditionalDAO) : BaseRepository<Additional>(additionalDAO){

    fun findAllByTowerId(tower_id: Long) = additionalDAO.getByTowerId(tower_id)

    fun findAllByCoordinateId(coord_id: Long) = additionalDAO.getByCoordinateId(coord_id)

    override fun getData(): LiveData<List<Additional>> = additionalDAO.getAll()

    override fun getById(id: Long): Additional? = additionalDAO.getById(id)

    override fun deleteAll() = additionalDAO.deleteAll()

    override fun findAll(): List<Additional> =
        additionalDAO.getWithParameters(QueryBuilder.buildSelectAllQuery(Additional::class.java))

}