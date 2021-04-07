package com.example.myapplication.database.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.database.dao.AdditionalDAO
import com.example.myapplication.database.entity.Additional

class AdditionalRepository(private val additionalDAO: AdditionalDAO) {

    val allAdditionalData: LiveData<List<Additional>> = additionalDAO.getAll()

    fun addAdditional(additional: Additional) = additionalDAO.insert(additional)

    fun addAllAdditionals(additionals: List<Additional>) = additionalDAO.insert(additionals)

    fun findById(additionalId: Long) = additionalDAO.getById(additionalId)

    fun findAllByTowerId(towerId: Long) = additionalDAO.getByTowerId(towerId)

    fun updateAdditional(additional: Additional){
        additionalDAO.update(additional)
    }

    fun deleteAdditional(additional: Additional){
        additionalDAO.delete(additional)
    }

    fun deleteAllAdditional(){
        additionalDAO.deleteAll()
    }

}