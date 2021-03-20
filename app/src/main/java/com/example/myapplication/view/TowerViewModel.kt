package com.example.myapplication.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.App
import com.example.myapplication.entity.Tower
import com.example.myapplication.repository.TowerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TowerViewModel(application: Application) : AndroidViewModel(application){

    private val readAllData: LiveData<List<Tower>>
    private val repository: TowerRepository

    init {
        val towerDao = App.getDatabaseManager().towerDao()
        repository = TowerRepository(towerDao)
        readAllData = repository.allTowersData

        readAllData.observeForever { it ->
            it.forEach {
                println(it)
            }
        }
    }

    fun addTower(tower: Tower){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTower(tower)
        }
    }

}