package com.example.datamanager.database.repository.impl

import androidx.lifecycle.LiveData
import com.example.datamanager.database.dao.TowerDAO
import com.example.datamanager.database.entity.Tower
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.datamanager.database.entity.Passport
import com.example.datamanager.database.repository.Repository
import com.example.datamanager.utli.QueryBuilder

class TowerRepository(private val towerDao: TowerDAO) : Repository<Tower> {

    fun findAllByPassportId(passport_id: Long) = towerDao.getByPassportId(passport_id)

    fun findAllByCoordinateId(coord_id: Long) = towerDao.getByCoordinateId(coord_id)

    override fun getData(): LiveData<List<Tower>> = towerDao.getAll()

    override fun getById(id: Long): Tower? = towerDao.getById(id)

    override fun add(addObject: Tower)= towerDao.insert(addObject)

    override fun addAll(addObjects: List<Tower>) =  towerDao.insert(addObjects)

    override fun update(updateObject: Tower) = towerDao.update(updateObject)

    override fun delete(deleteObject: Tower) = towerDao.delete(deleteObject)

    override fun deleteAll() = towerDao.deleteAll()

    override fun findWithParameters(query: SupportSQLiteQuery) =
        towerDao.getWithParameters(query)

    override fun findCurrentWithParameter(query: SupportSQLiteQuery) =
        towerDao.getCurrentObjectWithParameters(query)

    override fun findAll(): List<Tower> =
        towerDao.getWithParameters(QueryBuilder.buildSelectAllQuery(Tower::class.java))
}