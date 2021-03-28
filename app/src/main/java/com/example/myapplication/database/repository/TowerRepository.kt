package com.example.myapplication.database.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.database.dao.TowerDAO
import com.example.myapplication.database.entity.Tower

class TowerRepository(private val towerDao: TowerDAO) {

    val allTowersData: LiveData<List<Tower>> = towerDao.getAll()

    suspend fun addTower(tower: Tower){
        towerDao.insert(tower)
    }

    suspend fun updateTower(tower: Tower){
        towerDao.update(tower)
    }

    suspend fun deleteTower(tower: Tower){
        towerDao.delete(tower)
    }

    suspend fun deleteAllTower(){
        towerDao.deleteAll()
    }

}