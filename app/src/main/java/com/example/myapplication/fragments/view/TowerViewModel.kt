package com.example.myapplication.fragments.view

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.App
import com.example.myapplication.database.entity.Tower
import com.example.myapplication.database.repository.TowerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TowerViewModel(application: Application) : AndroidViewModel(application){

    val readAllData: LiveData<List<Tower>>
    private val repository: TowerRepository

    init {
        val towerDao = App.getDatabaseManager().towerDao()
        repository = TowerRepository(towerDao)
        readAllData = repository.allTowersData

        readAllData.observeForever { it ->
            it.forEach {
                Log.i("TEST", it.toString())
            }
        }
    }

    fun addTower(tower: Tower){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTower(tower)
        }
    }

    fun updateTower(tower: Tower){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateTower(tower)
        }
    }

    fun deleteTower(tower: Tower){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteTower(tower)
        }
    }

    fun deleteAllTowers() {
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllTower()
        }
    }

}