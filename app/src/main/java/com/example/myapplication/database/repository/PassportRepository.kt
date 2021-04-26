package com.example.myapplication.database.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.myapplication.database.dao.PassportDAO
import com.example.myapplication.database.entity.Passport

class PassportRepository(private val passportDAO: PassportDAO) {

    val allPassportsData: LiveData<List<Passport>> = passportDAO.getAll()

    fun addPassport(passport: Passport): Long = passportDAO.insert(passport)

    fun addPassports(passports: List<Passport>) = passportDAO.insert(passports)

    fun findPassportById(passport_id: Long) =  passportDAO.getById(passport_id)

    fun findPassportWithParameters(query: SupportSQLiteQuery) = passportDAO.getCurrentObjectWithParameters(query)

    fun updatePassport(passport: Passport){
        passportDAO.update(passport)
    }

    fun deletePassport(passport: Passport){
        passportDAO.delete(passport)
    }
    fun deleteAllPassports(){
        passportDAO.deleteAll()
    }


}