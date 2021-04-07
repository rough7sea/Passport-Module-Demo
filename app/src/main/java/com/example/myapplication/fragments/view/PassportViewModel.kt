package com.example.myapplication.fragments.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.App
import com.example.myapplication.database.entity.Passport
import com.example.myapplication.database.repository.PassportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PassportViewModel(application: Application) : AndroidViewModel(application){

    val readAllData: LiveData<List<Passport>>
    private val repository: PassportRepository

    init {
        val passportDAO = App.getDatabaseManager().passportDao()
        repository = PassportRepository(passportDAO)
        readAllData = repository.allPassportsData

        readAllData.observeForever { it ->
            it.forEach {
                println(it)
            }
        }
    }

    fun addPassport(passport: Passport){
        viewModelScope.launch(Dispatchers.IO){
            repository.addPassport(passport)
        }
    }

    fun deleteAllPassports() {
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllPassports()
        }
    }

}