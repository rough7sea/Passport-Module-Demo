package com.example.myapplication.fragments.view

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datamanager.database.entity.Additional
import com.example.datamanager.database.repository.impl.AdditionalRepository
import com.example.myapplication.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdditionalViewModel(application: Application) : AndroidViewModel(application){

    internal val readAllData: LiveData<List<Additional>>
    private val repository: AdditionalRepository by lazy {
        App.getDataManager().getRepository(Additional::class.java) as AdditionalRepository
    }

    init {
        readAllData = repository.getData()
        readAllData.observeForever {
            Log.i("TEST", "Receive ${it.size} additionals")
        }
    }

    fun addAdditional(additional: Additional){
        viewModelScope.launch(Dispatchers.IO){
            repository.add(additional)
        }
    }

    fun deleteAllAdditionals(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAll()
        }
    }
}