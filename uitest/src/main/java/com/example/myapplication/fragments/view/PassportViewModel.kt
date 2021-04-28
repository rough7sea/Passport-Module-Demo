package com.example.myapplication.fragments.view

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datamanager.database.entity.Passport
import com.example.datamanager.database.repository.impl.PassportRepository
import com.example.myapplication.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PassportViewModel(application: Application) : AndroidViewModel(application){

    val readAllData: LiveData<List<Passport>>
    private val repository: PassportRepository by lazy {
        App.getDataManager().getRepository(Passport::class.java) as PassportRepository
    }

    init {
        readAllData = repository.getData()
        readAllData.observeForever {
            Log.i("TEST", "Receive ${it.size} passports")
        }
    }

    fun addPassport(passport: Passport){
        viewModelScope.launch(Dispatchers.IO){
            repository.add(passport)
        }
    }

    fun deleteAllPassports() {
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAll()
        }
    }

}