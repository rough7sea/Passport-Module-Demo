package com.example.myapplication.database.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.database.dao.PassportDAO
import com.example.myapplication.database.entity.Passport

class PassportRepository(private val passportDAO: PassportDAO) {

    val allPassportsData: LiveData<List<Passport>> = passportDAO.getAll()

    suspend fun addPassport(passport: Passport){
        passportDAO.insert(passport)
    }

    suspend fun updatePassport(passport: Passport){
        passportDAO.update(passport)
    }

    suspend fun deletePassport(passport: Passport){
        passportDAO.delete(passport)
    }
    suspend fun deleteAllPassports(){
        passportDAO.deleteAll()
    }


}