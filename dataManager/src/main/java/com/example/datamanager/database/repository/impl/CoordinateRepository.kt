package com.example.datamanager.database.repository.impl

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.datamanager.database.dao.CoordinateDAO
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.repository.Repository
import com.example.datamanager.utli.QueryBuilder

class CoordinateRepository(private val coordinateDAO: CoordinateDAO) : BaseRepository<Coordinate>(coordinateDAO){

    fun getCoordinateByLongitudeAndLatitude(latitude: Double, longitude: Double) =
        coordinateDAO.getByLongitudeAndLatitude(latitude, longitude)

    override fun getData(): LiveData<List<Coordinate>> = coordinateDAO.getAll()

    override fun getById(id: Long): Coordinate? = coordinateDAO.getById(id)

    override fun deleteAll()  = coordinateDAO.deleteAll()

    override fun findAll(): List<Coordinate> =
        coordinateDAO.getWithParameters(QueryBuilder.buildSelectAllQuery(Coordinate::class.java))

}