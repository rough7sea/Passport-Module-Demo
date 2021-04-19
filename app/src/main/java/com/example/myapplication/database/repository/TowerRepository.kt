package com.example.myapplication.database.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.database.dao.TowerDAO
import com.example.myapplication.database.entity.Tower
import androidx.sqlite.db.SupportSQLiteQuery

class TowerRepository(private val towerDao: TowerDAO) {

    val allTowersData: LiveData<List<Tower>> = towerDao.getAll()

    fun addTower(tower: Tower) : Long = towerDao.insert(tower)

    fun findAllByPassportId(passport_id: Long) = towerDao.getByPassportId(passport_id)

    fun findTowerById(tower_id: Long) = towerDao.getById(tower_id)

    fun findTowerWithParameters(query: SupportSQLiteQuery) = towerDao.getCurrentObjectWithParameters(query)

    fun addAllTowers(towers: List<Tower>){
        towerDao.insert(towers)
    }

    fun updateTower(tower: Tower){
        towerDao.update(tower)
    }

    fun deleteTower(tower: Tower){
        towerDao.delete(tower)
    }

    fun deleteAllTower(){
        towerDao.deleteAll()
    }

}