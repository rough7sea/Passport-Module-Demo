package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.dao.PassportDAO
import com.example.myapplication.entity.Passport

class PassportRepository(private val passportDAO: PassportDAO) {

    val allPassportsData: LiveData<List<Passport>> = passportDAO.getAll()

    suspend fun addPassport(passport: Passport){
        passportDAO.insert(passport)
    }

}