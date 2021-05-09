package com.example.datamanager.database.repository.impl

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.datamanager.database.dao.PassportDAO
import com.example.datamanager.database.entity.Passport
import com.example.datamanager.database.repository.Repository
import com.example.datamanager.utli.QueryBuilder

class PassportRepository(private val passportDAO: PassportDAO) : BaseRepository<Passport>(passportDAO){

    override fun getData(): LiveData<List<Passport>>  = passportDAO.getAll()

    override fun getById(id: Long): Passport? =  passportDAO.getById(id)

    override fun deleteAll() = passportDAO.deleteAll()

    override fun findAll(): List<Passport> =
        passportDAO.getWithParameters(QueryBuilder.buildSelectAllQuery(Passport::class.java))

}