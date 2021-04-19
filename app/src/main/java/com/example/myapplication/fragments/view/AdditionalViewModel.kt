package com.example.myapplication.fragments.view

import android.app.Application
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
    private val repository: AdditionalRepository

    init {
        val additionalDAO = App.getDatabaseManager().additionalDao()
        repository = AdditionalRepository(additionalDAO)
        readAllData = repository.allAdditionalData

        readAllData.observeForever { it ->
            it.forEach {
                println(it)
            }
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