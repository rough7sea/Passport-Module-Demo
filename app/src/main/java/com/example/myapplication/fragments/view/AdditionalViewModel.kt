package com.example.myapplication.fragments.view

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.App
import com.example.myapplication.database.entity.Additional
import com.example.myapplication.database.repository.AdditionalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdditionalViewModel(application: Application) : AndroidViewModel(application){

    internal val readAllData: LiveData<List<Additional>>
    private val repository: AdditionalRepository = AdditionalRepository(App.getDatabase().additionalDao())

    init {
        readAllData = repository.allAdditionalData
        repository.allAdditionalData.observeForever {
            Log.i("TEST", "Receive ${it.size} additionals")
        }
    }

    fun addAdditional(additional: Additional){
        viewModelScope.launch(Dispatchers.IO){
            repository.addAdditional(additional)
        }
    }

    fun deleteAllAdditionals(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllAdditionals()
        }
    }
}