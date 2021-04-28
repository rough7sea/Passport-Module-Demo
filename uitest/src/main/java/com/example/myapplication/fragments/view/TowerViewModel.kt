package com.example.myapplication.fragments.view

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datamanager.database.entity.Tower
import com.example.datamanager.database.repository.impl.TowerRepository
import com.example.myapplication.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TowerViewModel(application: Application) : AndroidViewModel(application){

    val readAllData: LiveData<List<Tower>>
    private val repository: TowerRepository by lazy {
        App.getDataManager().getRepository(Tower::class.java) as TowerRepository
    }

    init {
        readAllData = repository.getData()
        readAllData.observeForever {
            Log.i("TEST", "Receive ${it.size} towers")
        }
    }

    fun addTower(tower: Tower){
        viewModelScope.launch(Dispatchers.IO){
            repository.add(tower)
        }
    }

    fun updateTower(tower: Tower){
        viewModelScope.launch(Dispatchers.IO){
            repository.update(tower)
        }
    }

    fun deleteTower(tower: Tower){
        viewModelScope.launch(Dispatchers.IO){
            repository.delete(tower)
        }
    }

    fun deleteAllTowers() {
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAll()
        }
    }

}