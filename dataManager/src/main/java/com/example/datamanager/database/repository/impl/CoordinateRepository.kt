package com.example.datamanager.database.repository.impl

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.datamanager.database.dao.CoordinateDAO
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.repository.Repository
import com.example.datamanager.utli.QueryBuilder
import java.util.*

class CoordinateRepository(private val coordinateDAO: CoordinateDAO) : BaseRepository<Coordinate>(coordinateDAO){

    fun getCoordinateByLongitudeAndLatitude(latitude: Double, longitude: Double) =
        coordinateDAO.getByLongitudeAndLatitude(latitude, longitude)

    fun getOrCreateCoordinateId(latitude: Double, longitude: Double): Long{
        var coord_id = this.add(Coordinate(0, Date(), latitude, longitude))
        if (coord_id == -1L){
            coord_id = this.getCoordinateByLongitudeAndLatitude(latitude, longitude)!!.coord_id
        }
        return coord_id
    }

    fun deleteById(id: Long) = coordinateDAO.deleteById(id)

    override fun getData(): LiveData<List<Coordinate>> = coordinateDAO.getAll()

    override fun getById(id: Long): Coordinate? = coordinateDAO.getById(id)

    override fun deleteAll()  = coordinateDAO.deleteAll()

    override fun findAll(): List<Coordinate> =
        coordinateDAO.getWithParameters(QueryBuilder.buildSelectAllQuery(Coordinate::class.java))

}