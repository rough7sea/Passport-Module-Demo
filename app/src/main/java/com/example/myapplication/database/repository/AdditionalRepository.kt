package com.example.myapplication.database.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.database.dao.AdditionalDAO
import com.example.myapplication.database.entity.Additional

class AdditionalRepository(private val additionalDAO: AdditionalDAO) {

    val allAdditionalData: LiveData<List<Additional>> = additionalDAO.getAll()

    suspend fun addAdditional(additional: Additional){
        additionalDAO.insert(additional)
    }

    suspend fun updateAdditional(additional: Additional){
        additionalDAO.update(additional)
    }

    suspend fun deleteAdditional(additional: Additional){
        additionalDAO.delete(additional)
    }

    suspend fun deleteAllAdditional(additional: Additional){
        additionalDAO.deleteAll()
    }

}