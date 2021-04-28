package com.example.datamanager.database.repository.impl

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.datamanager.database.dao.PassportDAO
import com.example.datamanager.database.entity.Passport
import com.example.datamanager.database.repository.Repository
import com.example.datamanager.utli.QueryBuilder

class PassportRepository(private val passportDAO: PassportDAO) : Repository<Passport>{

    override fun getData(): LiveData<List<Passport>>  = passportDAO.getAll()

    override fun getById(id: Long): Passport? =  passportDAO.getById(id)

    override fun add(addObject: Passport): Long = passportDAO.insert(addObject)

    override fun addAll(addObjects: List<Passport>) = passportDAO.insert(addObjects)

    override fun update(updateObject: Passport) = passportDAO.update(updateObject)

    override fun delete(deleteObject: Passport) = passportDAO.delete(deleteObject)

    override fun deleteAll() = passportDAO.deleteAll()

    override fun findWithParameters(query: SupportSQLiteQuery): List<Passport> =
        passportDAO.getWithParameters(query)

    override fun findCurrentWithParameter(query: SupportSQLiteQuery): Passport? =
        passportDAO.getCurrentObjectWithParameters(query)

    override fun findAll(): List<Passport> =
        passportDAO.getWithParameters(QueryBuilder.buildSelectAllQuery(Passport::class.java))

}