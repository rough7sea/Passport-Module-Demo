package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.dao.TowerDAO
import com.example.myapplication.entity.Tower

class TowerRepository(private val towerDao: TowerDAO) {

    val allTowersData: LiveData<List<Tower>> = towerDao.getAll()

    suspend fun addTower(tower: Tower){
        towerDao.insert(tower)
    }

}