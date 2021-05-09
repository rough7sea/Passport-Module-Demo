package com.example.datamanager.database.repository.impl

import androidx.lifecycle.LiveData
import com.example.datamanager.database.dao.TowerDAO
import com.example.datamanager.database.entity.Tower
import com.example.datamanager.utli.QueryBuilder

class TowerRepository(private val towerDao: TowerDAO) : BaseRepository<Tower>(towerDao) {

    fun findAllByPassportId(passport_id: Long) = towerDao.getByPassportId(passport_id)

    fun findAllByCoordinateId(coord_id: Long) = towerDao.getByCoordinateId(coord_id)

    override fun getData(): LiveData<List<Tower>> = towerDao.getAll()

    override fun getById(id: Long): Tower? = towerDao.getById(id)

    override fun deleteAll() = towerDao.deleteAll()

    override fun findAll(): List<Tower> =
        towerDao.getWithParameters(QueryBuilder.buildSelectAllQuery(Tower::class.java))
}