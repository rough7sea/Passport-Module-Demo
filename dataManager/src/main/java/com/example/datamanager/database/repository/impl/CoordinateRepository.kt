package com.example.datamanager.database.repository.impl

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.datamanager.database.dao.CoordinateDAO
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.repository.Repository
import com.example.datamanager.utli.QueryBuilder

class CoordinateRepository(private val coordinateDAO: CoordinateDAO) : Repository<Coordinate>{

    fun getCoordinateByLongitudeAndLatitude(longitude: Double, latitude: Double) =
        coordinateDAO.getByLongitudeAndLatitude(longitude, latitude)

    override fun getData(): LiveData<List<Coordinate>> = coordinateDAO.getAll()

    override fun getById(id: Long): Coordinate? = coordinateDAO.getById(id)

    override fun add(addObject: Coordinate): Long = coordinateDAO.insert(addObject)

    override fun addAll(addObjects: List<Coordinate>) = coordinateDAO.insert(addObjects)

    override fun update(updateObject: Coordinate) = coordinateDAO.update(updateObject)

    override fun delete(deleteObject: Coordinate) = coordinateDAO.delete(deleteObject)

    override fun deleteAll()  = coordinateDAO.deleteAll()

    override fun findWithParameters(query: SupportSQLiteQuery): List<Coordinate> =
        coordinateDAO.getWithParameters(query)

    override fun findCurrentWithParameter(query: SupportSQLiteQuery): Coordinate? =
        coordinateDAO.getCurrentObjectWithParameters(query)

    override fun findAll(): List<Coordinate> =
        coordinateDAO.getWithParameters(QueryBuilder.buildSelectAllQuery(Coordinate::class.java))

}